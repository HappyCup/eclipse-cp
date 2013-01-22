package cpjavaparser.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;

class TypeSearchVisitor extends ASTVisitor {

	private Set<ITypeBinding> encounteredPublicTypes;
	private Set<ITypeBinding> encounteredPrivateTypes;
	private ITypeBinding superClass;
	private Set<ITypeBinding> implementedInterfaces;

	public TypeSearchVisitor() {
		encounteredPublicTypes = new HashSet<ITypeBinding>();
		encounteredPrivateTypes = new HashSet<ITypeBinding>();
		implementedInterfaces = new HashSet<ITypeBinding>();
		superClass = null;
	}

	public Set<ITypeBinding> getPublicTypeBindings() {
		return encounteredPublicTypes;
	}

	public Set<ITypeBinding> getPrivateTypeBindings() {
		return encounteredPrivateTypes;
	}

	public Set<ITypeBinding> getImplementedInterfaces() {
		return implementedInterfaces;
	}

	public ITypeBinding getSuperClass() {
		return superClass;
	}

	@Override
	public boolean visit(TypeDeclaration node) {
		// Only Top-Level Types
		if (node.isPackageMemberTypeDeclaration()) {
			Type superClassType = node.getSuperclassType();
			if (superClassType != null) {
				superClass = superClassType.resolveBinding().getErasure();
			} else {
				superClass = node.getAST().resolveWellKnownType("Object");
			}
			for (Object interfaceObject : node.superInterfaceTypes()) {
				Type interfaceType = (Type) interfaceObject;
				ITypeBinding interfaceBinding = interfaceType.resolveBinding()
						.getErasure();
				implementedInterfaces.add(interfaceBinding);
			}
		}
		return super.visit(node);
	}

	@Override
	public boolean visit(FieldDeclaration field) {
		Type fieldType = field.getType();
		Collection<ITypeBinding> typeBindings = getContainedTypeBindings(fieldType);
		int modifiers = field.getModifiers();
		if (Modifier.isPublic(modifiers)) {
			encounteredPublicTypes.addAll(typeBindings);
		} else {
			encounteredPrivateTypes.addAll(typeBindings);
		}
		return true;
	}

	@Override
	public boolean visit(MethodDeclaration method) {
		Type returnType = method.getReturnType2(); // getReturnType is
													// deprecated
		if (returnType == null)
			return super.visit(method); // for void-methods
		Collection<ITypeBinding> returnTypeBindings = getContainedTypeBindings(returnType);
		int modifiers = method.getModifiers();
		if (Modifier.isPublic(modifiers)) {
			encounteredPublicTypes.addAll(returnTypeBindings);
		} else {
			encounteredPrivateTypes.addAll(returnTypeBindings);
		}

		return true;
	}

	@Override
	public boolean visit(SingleVariableDeclaration variable) {
		Type variableType = variable.getType();
		Collection<ITypeBinding> typeBindings = getContainedTypeBindings(variableType);
		int modifiers = variable.getModifiers();
		if (Modifier.isPublic(modifiers)) {
			encounteredPublicTypes.addAll(typeBindings);
		} else {
			encounteredPrivateTypes.addAll(typeBindings);
		}
		return true;
	}

	@Override
	public boolean visit(VariableDeclarationExpression variable) {
		Type variableType = variable.getType();
		Collection<ITypeBinding> typeBindings = getContainedTypeBindings(variableType);
		int modifiers = variable.getModifiers();
		if (Modifier.isPublic(modifiers)) {
			encounteredPublicTypes.addAll(typeBindings);
		} else {
			encounteredPrivateTypes.addAll(typeBindings);
		}
		return true;
	}

	@Override
	public boolean visit(ClassInstanceCreation node) {
		Type type = node.getType();
		Collection<ITypeBinding> typeBindings = getContainedTypeBindings(type);
		encounteredPrivateTypes.addAll(typeBindings);
		return true;

	}

	private Collection<ITypeBinding> getContainedTypeBindings(Type type) {
		Collection<ITypeBinding> result;
		if (type.isSimpleType() || type.isQualifiedType()) {
			ITypeBinding bind = type.resolveBinding();
			result = new ArrayList<ITypeBinding>(1);
			if (!bind.isTypeVariable()) { // TODO Special treatment for Type
											// Variables
				result.add(bind.getErasure());
			}
		} else if (type.isArrayType()) {
			ArrayType arrType = (ArrayType) type;
			Type elementType = arrType.getElementType();
			result = getContainedTypeBindings(elementType);
		} else if (type.isParameterizedType()) {
			ParameterizedType parType = (ParameterizedType) type;
			result = new ArrayList<ITypeBinding>();
			result.addAll(getContainedTypeBindings(parType.getType()));
			for (Object argumentObject : parType.typeArguments()) {
				Type argumentType = (Type) argumentObject;
				result.addAll(getContainedTypeBindings(argumentType));
			}
		} else {
			result = new ArrayList<ITypeBinding>(0);
		}
		return result;
	}

	@Override
	public boolean visit(MethodInvocation node) {
		Expression exp = node.getExpression();
		checkForStaticReference(exp);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(FieldAccess node) {
		checkForStaticReference(node.getExpression());
		return super.visit(node);
	}
	
	@Override
	public boolean visit(QualifiedName node) { //AST can not distinguish between FieldAccess and QualifiedName
		checkForStaticReference(node.getQualifier());
		return super.visit(node);
	}
	
	private void checkForStaticReference(Expression exp){
		if (exp!=null && exp.getNodeType() == Expression.SIMPLE_NAME){
			SimpleName name = (SimpleName) exp;
			IBinding bind = name.resolveBinding();
			if (bind.getKind() == IBinding.TYPE){
				ITypeBinding typeBind = (ITypeBinding) bind;
				encounteredPrivateTypes.add(typeBind.getErasure());
			}
		}
	}
}
