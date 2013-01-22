package cpjavaparser.parser;

import java.util.Collection;
import java.util.HashSet;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.IProblem;

import cpjavaparser.values.TypeName;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ClassParser {

	private Collection<TypeName> referencedPrivateTypeNames;
	private Collection<TypeName> referencedPublicTypeNames;
	private Collection<TypeName> implementedInterfacesNames;
	private TypeName superClass;
	private SimpleName _simpleClassName;
	private Name _packageName;
	private boolean _interface;

	public ClassParser(ICompilationUnit currentClass) throws JavaModelException {

		referencedPrivateTypeNames = new HashSet<TypeName>();
		referencedPublicTypeNames = new HashSet<TypeName>();
		implementedInterfacesNames = new HashSet<TypeName>();
		_interface = false;

		ASTParser astParser = ASTParser.newParser(AST.JLS3);

		astParser.setKind(ASTParser.K_COMPILATION_UNIT);

		astParser.setSource(currentClass);
		astParser.setResolveBindings(true);
		CompilationUnit root = (CompilationUnit) astParser.createAST(null); // TODO
																			// IProgressMonitor
																			// statt
																			// null
		for (IProblem prob : root.getProblems()) {
			System.out.println(prob.getMessage());
		}

		root.accept(new ClassInfoVisitor());

		TypeSearchVisitor typeSearch = new TypeSearchVisitor();
		root.accept(typeSearch);
		for (ITypeBinding typeBinding : typeSearch.getPrivateTypeBindings()) {
			if (!typeBinding.isPrimitive()) {
				referencedPrivateTypeNames.add(TypeName.valueOf(typeBinding));
			}
		}
		for (ITypeBinding typeBinding : typeSearch.getPublicTypeBindings()) {
			if (!typeBinding.isPrimitive()) {
				referencedPublicTypeNames.add(TypeName.valueOf(typeBinding));
			}
		}
		for (ITypeBinding typeBinding : typeSearch.getImplementedInterfaces()) {
			implementedInterfacesNames.add(TypeName.valueOf(typeBinding));
		}
		referencedPrivateTypeNames.removeAll(referencedPublicTypeNames);
		if (_interface) {
			referencedPublicTypeNames.addAll(referencedPrivateTypeNames);
		}
		if (typeSearch.getSuperClass() != null) {
			superClass = TypeName.valueOf(typeSearch.getSuperClass());
		}
	}

	public TypeName getClassName() {
		return TypeName.valueOf(_packageName, _simpleClassName);
	}

	public Collection<TypeName> getReferencedPublicTypes() {
		return referencedPublicTypeNames;
	}

	public Collection<TypeName> getReferencedPrivateTypes() {
		return referencedPrivateTypeNames;
	}

	public Collection<TypeName> getImplementedInterfaces() {
		return implementedInterfacesNames;
	}

	public TypeName getSuperClass() {
		return superClass;
	}

	private class ClassInfoVisitor extends ASTVisitor {
		@Override
		public boolean visit(PackageDeclaration node) {
			_packageName = node.getName();
			return true;
		}

		@Override
		public boolean visit(TypeDeclaration node) {
			// Only Top-Level Types
			if (node.isPackageMemberTypeDeclaration()) {
				_simpleClassName = node.getName();
				_interface = node.isInterface();
			}
			return true;
		}
	}

}
