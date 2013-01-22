package cpjavaparser.parser;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

import cpjavaparser.values.RelationshipName;
import cpjavaparser.values.TypeName;
import cpview.values.CPMode;
import cpview.values.RelationshipType;

public class Parser {

	private Set<TypeName> _classes;
	private Set<RelationshipName> _relationship;

	public Parser() {
		_classes = new HashSet<TypeName>();
		_relationship = new HashSet<RelationshipName>();
	}

	/**
	 * Evaluates all packages in the given Java Project. Finds all Classes and
	 * relationships in the project
	 * 
	 * @param project
	 *            The Project to evaluate
	 * @throws JavaModelException
	 */
	public void evaluateProject(IJavaProject project) throws JavaModelException {
		IPackageFragment[] packages = project.getPackageFragments();
		for (IPackageFragment pack : packages) {
			if (pack.getKind() == IPackageFragmentRoot.K_SOURCE) {
				evaluatePackage(pack);
			}
		}
	}

	/**
	 * Evaluates all classes in the given package. Finds all relationships in
	 * the package as well as relationships coming from this package to other
	 * classes in the project.
	 * 
	 * @param pack
	 *            The package to evaluate
	 * @throws JavaModelException
	 */
	public void evaluatePackage(IPackageFragment pack)
			throws JavaModelException {
		ICompilationUnit[] units = pack.getCompilationUnits();
		for (ICompilationUnit unit : units) {
			evaluateCompilationUnit(unit);
		}
	}

	/**
	 * Evaluates the given CompilationUnit. Finds all relationships originating
	 * from this class.
	 * 
	 * @param unit
	 *            The CompilationUnit (or Class) to evaluate
	 * @throws JavaModelException
	 */
	public void evaluateCompilationUnit(ICompilationUnit unit)
			throws JavaModelException {
		for (IType type : unit.getTypes()) {
			ClassParser classparser = new ClassParser(unit);
			_classes.add(classparser.getClassName());
			for (TypeName name : classparser.getReferencedPublicTypes()) {
				RelationshipName rel = new RelationshipName(
						classparser.getClassName(), name, CPMode.CONSUME,
						RelationshipType.ASSOCIATION);
				_relationship.add(rel);
			}
			for (TypeName name : classparser.getReferencedPrivateTypes()) {
				RelationshipName rel = new RelationshipName(
						classparser.getClassName(), name, CPMode.PRODUCE,
						RelationshipType.ASSOCIATION);
				_relationship.add(rel);
			}
			for (TypeName name : classparser.getImplementedInterfaces()) {
				RelationshipName rel = new RelationshipName(
						classparser.getClassName(), name, CPMode.CONSUME,
						RelationshipType.INHERITENCE);
				_relationship.add(rel);
			}
			if (classparser.getSuperClass() != null){
			RelationshipName superClassRel = new RelationshipName(
					classparser.getClassName(), classparser.getSuperClass(),
					CPMode.CONSUME, RelationshipType.INHERITENCE);
			_relationship.add(superClassRel);
			}

		}
	}

	/**
	 * @return All classes found in evaluations
	 */
	public Set<TypeName> getClasses() {
		return _classes;
	}

	public Set<RelationshipName> getRelationships() {
		return _relationship;
	}

}
