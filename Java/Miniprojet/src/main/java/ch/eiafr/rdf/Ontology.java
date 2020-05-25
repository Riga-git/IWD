package ch.eiafr.rdf;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.rdf4j.RDF4JException;
import org.eclipse.rdf4j.common.iteration.Iterations;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.model.vocabulary.XMLSchema;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

public class Ontology extends LRI{
	
	static Map<String, IRI> iriMap = new HashMap<String, IRI>();
	
	public static void main(String[] args) {

		LRI.createIri();
		// file path
		String basepath = new File("").getAbsolutePath();
		// Repo path
		File dataDir = new File(basepath + "..\\..\\MyRepository");
		// Create repository
		Repository rep = new SailRepository(new MemoryStore(dataDir));

		iriMap = createIndividuals(rep);
		buildOntology(rep);
		
		RepositoryConnection conn = rep.getConnection();
		RepositoryResult<Statement> statements = conn.getStatements(null, null, null, true);
		
		Model model = Iterations.addAll(statements, new LinkedHashModel());
		model.setNamespace("rdf", RDF.NAMESPACE);
		model.setNamespace("rdfs", RDFS.NAMESPACE);
		model.setNamespace("xsd", XMLSchema.NAMESPACE);
		model.setNamespace("foaf", FOAF.NAMESPACE);
		model.setNamespace("lri", LRI.NAMESPACE);
		
		Rio.write(model, System.out, RDFFormat.TURTLE);

	}
	
	static void buildOntology(Repository rep) {
		
		
		RepositoryConnection conn = rep.getConnection();
		
		conn.add(iriMap.get("julien"), LRI.INCHARGEOF, iriMap.get("frenchCourse"));
		conn.add(iriMap.get("luca"), LRI.INCHARGEOF, iriMap.get("germanCourse"));
		conn.add(iriMap.get("desire"), LRI.FOLLOWS, iriMap.get("germanCourse"));
		conn.add(iriMap.get("jerome"), LRI.FOLLOWS, iriMap.get("frenchCourse"));
		conn.add(iriMap.get("inlinguo"), LRI.HASCLASSROOM, iriMap.get("room_132"));
		conn.add(iriMap.get("inlinguo"), LRI.HASCLASSROOM, iriMap.get("room_256"));
		conn.add(iriMap.get("room_132"), LRI.HASSCHOOLFURNITURE, iriMap.get("table"));
		conn.add(iriMap.get("room_256"), LRI.HASSCHOOLFURNITURE, iriMap.get("whiteBoard"));
		conn.add(iriMap.get("desire"), LRI.LIVES, iriMap.get("desireAddress"));
		conn.add(iriMap.get("luca"), LRI.LIVES, iriMap.get("lucaAddress"));
		conn.add(iriMap.get("jerome"), LRI.LIVES, iriMap.get("jeromeAddress"));
		conn.add(iriMap.get("julien"), LRI.LIVES, iriMap.get("julienAddress"));
		conn.add(iriMap.get("julien"), LRI.LOCATED, iriMap.get("schooAddress"));
		
		conn.add(LRI.SCHOOLFURNITURE, RDFS.SUBCLASSOF, LRI.FURNITURE);
		
	}

	static Map<String, IRI> createIndividuals(Repository rep) {
		// Examples
		
		Map<String, IRI> map = new HashMap<String, IRI>();
		map.put("jerome", createStudent(rep, "Jerome", "Jerôme Garo", "12.03"));
		map.put("desire", createStudent(rep, "Desire", "Desire Nonis", "12.03"));
		map.put("luca", createProf(rep, "Luca", "Luca Rigazzi", "Italian"));
		map.put("julien", createProf(rep, "Julien", "Julien Tscherig", "French"));
		map.put("inlinguo", createSchool(rep, "Inlinguo", "Inlinguo", "Language School"));
		map.put("germanCourse", createCourse(rep, "GermanCourse", "B2", "German"));
		map.put("frenchCourse", createCourse(rep, "FrenchCourse", "C1", "FrenchS"));
		
		map.put("room_132", createClassRoom(rep, "Room_132", 132, 32));
		map.put("room_256", createClassRoom(rep, "Room_256", 256, 12));
		map.put("whiteBoard", createSchoolFurniture(rep, "WhiteBoard", "White Board", 250 , "Math class"));
		map.put("table", createSchoolFurniture(rep, "Table", "Table", 320, "Chemistry", 20, "Wood"));
		map.put("schooAddress", createAddress(rep, "SchooAddress", 1400, "Yverdon", "Rue des langues 12"));
		map.put("lucaAddress", createAddress(rep, "LucaAddress", 1422, "Grandson", "Rue des Jardins 22"));							
		map.put("julienAddress", createAddress(rep, "JulienAddress", 1212, "Lorem", "Ipsum 45"));		
		map.put("desireAddress", createAddress(rep, "DesireAddress", 3232, "Nunningen", "Lebernweg 5"));		
		map.put("jeromeAddress", createAddress(rep, "JeromeAddress", 1400, "Yverdon", "Rue des langues 12"));		
		
		return map;
	}

	// Address
	static IRI createAddress(Repository rep, String identifier, int cap, String city, String Road) {

		ValueFactory f = rep.getValueFactory();

		IRI iri = f.createIRI(LRI.NAMESPACE, identifier);

		RepositoryConnection conn = rep.getConnection();

		try {
			System.out.println(LRI.ADDRESS);
			conn.add(iri, RDF.TYPE, LRI.ADDRESS);
			conn.add(iri, RDFS.LABEL, f.createLiteral(identifier, XMLSchema.STRING));
			conn.add(iri, LRI.CAP, f.createLiteral(cap));
			conn.add(iri, LRI.CITY, f.createLiteral(city, XMLSchema.STRING));
			conn.add(iri, LRI.ROAD, f.createLiteral(Road, XMLSchema.STRING));
		}	
		catch (RDF4JException e) {
			System.out.println("Exception : " + e.toString());
		}  
		finally {
			conn.close();
		}

		return iri;
	}

	// Prof
	static IRI createProf(Repository rep, String identifier, String name, String specialization) {

		ValueFactory f = rep.getValueFactory();

		IRI iri = f.createIRI(LRI.NAMESPACE, identifier);

		RepositoryConnection conn = rep.getConnection();

		try {
			conn.add(iri, RDF.TYPE, LRI.PROF);
			conn.add(iri, RDFS.LABEL, f.createLiteral(identifier, XMLSchema.STRING));
			conn.add(iri, RDFS.SUBCLASSOF, FOAF.PERSON);
			conn.add(iri, FOAF.NAME, f.createLiteral(name, XMLSchema.STRING));
			conn.add(iri, LRI.SPECIALIZATION, f.createLiteral(name, XMLSchema.STRING));
		}	
		catch (RDF4JException e) {
			System.out.println("Exception : " + e.toString());
		}  
		finally {
			conn.close();
		}
		return iri;
	}

	// Student
	static IRI createStudent(Repository rep, String identifier, String name, String registrationDate) {

		ValueFactory f = rep.getValueFactory();

		IRI iri = f.createIRI(LRI.NAMESPACE, identifier);

		RepositoryConnection conn = rep.getConnection();

		try {
			conn.add(iri, RDF.TYPE, LRI.STUDENT);
			conn.add(iri, RDFS.LABEL, f.createLiteral(identifier, XMLSchema.STRING));
			conn.add(iri, RDFS.SUBCLASSOF, FOAF.PERSON);
			conn.add(iri, FOAF.NAME, f.createLiteral(name, XMLSchema.STRING));
			conn.add(iri, LRI.REGISRATIONDATE, f.createLiteral(registrationDate, XMLSchema.STRING));
		}	
		catch (RDF4JException e) {
			System.out.println("Exception : " + e.toString());
		}  
		finally {
			conn.close();
		}
		return iri;
	}

	// Course
	static IRI createCourse(Repository rep, String identifier, String level, String language) {

		ValueFactory f = rep.getValueFactory();

		IRI iri = f.createIRI(LRI.NAMESPACE, identifier);

		RepositoryConnection conn = rep.getConnection();

		try {
			conn.add(iri, RDF.TYPE, LRI.COURSE);
			conn.add(iri, RDFS.LABEL, f.createLiteral(identifier, XMLSchema.STRING));
			conn.add(iri, LRI.LEVEL, f.createLiteral(level, XMLSchema.STRING));
			conn.add(iri, LRI.LANGUAGE, f.createLiteral(language, XMLSchema.STRING));
		}	
		catch (RDF4JException e) {
			System.out.println("Exception : " + e.toString());
		}  
		finally {
			conn.close();
		}
		return iri;
	}

	// Public school
	static IRI createSchool(Repository rep, String identifier, String Name, String schoolType) {

		ValueFactory f = rep.getValueFactory();

		IRI iri = f.createIRI(LRI.NAMESPACE, identifier);

		RepositoryConnection conn = rep.getConnection();

		try {
			conn.add(iri, RDF.TYPE, LRI.SCHOOL);
			conn.add(iri, RDFS.LABEL, f.createLiteral(identifier, XMLSchema.STRING));
			conn.add(iri, RDFS.SUBCLASSOF, LRI.PUBLICSERVICE);
			conn.add(iri, RDFS.SUBCLASSOF, LRI.BUILDING);
			conn.add(iri, LRI.NAME, f.createLiteral(Name, XMLSchema.STRING));
			conn.add(iri, LRI.SCHOOLTYPE, f.createLiteral(schoolType, XMLSchema.STRING));
		}	
		catch (RDF4JException e) {
			System.out.println("Exception : " + e.toString());
		}  
		finally {
			conn.close();
		}
		return iri;
	}

	// Public service
	static IRI createPublicService(Repository rep, String identifier, String name, String schoolType) {

		ValueFactory f = rep.getValueFactory();

		IRI iri = f.createIRI(LRI.NAMESPACE, identifier);

		RepositoryConnection conn = rep.getConnection();

		try {
			conn.add(iri, RDF.TYPE, LRI.PUBLICSERVICE);
			conn.add(iri, RDFS.LABEL, f.createLiteral(identifier, XMLSchema.STRING));
			conn.add(iri, LRI.SERVICETYPE, f.createLiteral(name, XMLSchema.STRING));
			conn.add(iri, LRI.OPENINGHOURS, f.createLiteral(schoolType, XMLSchema.STRING));
		}	
		catch (RDF4JException e) {
			System.out.println("Exception : " + e.toString());
		}  
		finally {
			conn.close();
		}
		return iri;
	}

	// Class room
	static IRI createClassRoom(Repository rep, String identifier, int number, int capacity) {

		ValueFactory f = rep.getValueFactory();

		IRI iri = f.createIRI(LRI.NAMESPACE, identifier);

		RepositoryConnection conn = rep.getConnection();

		try {
			conn.add(iri, RDF.TYPE, LRI.CLASSROOM);
			conn.add(iri, RDFS.LABEL, f.createLiteral(identifier, XMLSchema.STRING));
			conn.add(iri, RDFS.SUBCLASSOF, LRI.ROOM);
			conn.add(iri, LRI.CAPACITY, f.createLiteral(number));
			conn.add(iri, LRI.NUMBER, f.createLiteral(capacity));
		}	
		catch (RDF4JException e) {
			System.out.println("Exception : " + e.toString());
		}  
		finally {
			conn.close();
		}
		return iri;
	}

	// Class Building
	static IRI createBuilding(Repository rep, String identifier, String color, int height) {

		ValueFactory f = rep.getValueFactory();

		IRI iri = f.createIRI(LRI.NAMESPACE, identifier);

		RepositoryConnection conn = rep.getConnection();

		try {
			conn.add(iri, RDF.TYPE, LRI.BUILDING);
			conn.add(iri, RDFS.LABEL, f.createLiteral(identifier, XMLSchema.STRING));
			conn.add(iri, LRI.COLOR, f.createLiteral(color, XMLSchema.STRING));
			conn.add(iri, LRI.HEIGHT, f.createLiteral(height));
		}	
		catch (RDF4JException e) {
			System.out.println("Exception : " + e.toString());
		}  
		finally {
			conn.close();
		}
		return iri;
	}

	// Room
	static IRI createRoom(Repository rep, String identifier, int surface, int volume) {

		ValueFactory f = rep.getValueFactory();

		IRI iri = f.createIRI(LRI.NAMESPACE, identifier);

		RepositoryConnection conn = rep.getConnection();

		try {
			conn.add(iri, RDF.TYPE, LRI.ROOM);
			conn.add(iri, RDFS.LABEL, f.createLiteral(identifier, XMLSchema.STRING));
			conn.add(iri, LRI.SURFACE, f.createLiteral(surface));
			conn.add(iri, LRI.VOLUME, f.createLiteral(volume));
		}	
		catch (RDF4JException e) {
			System.out.println("Exception : " + e.toString());
		}  
		finally {
			conn.close();
		}
		return iri;
	}

	// School furniture
	static IRI createSchoolFurniture(Repository rep, String identifier, String name, float price, String subject) {

		ValueFactory f = rep.getValueFactory();

		IRI iri = f.createIRI(LRI.NAMESPACE, identifier);

		RepositoryConnection conn = rep.getConnection();

		try {
			conn.add(iri, RDF.TYPE, LRI.SCHOOLFURNITURE);
			conn.add(iri, RDFS.LABEL, f.createLiteral(identifier, XMLSchema.STRING));
			//conn.add(iri, RDFS.SUBCLASSOF, LRI.FURNITURE);
			conn.add(iri, LRI.PRICE, f.createLiteral(price));
			conn.add(iri, LRI.SUBJECT, f.createLiteral(subject, XMLSchema.STRING));
			conn.add(iri, LRI.SUBJECT, f.createLiteral(name, XMLSchema.STRING));
		}	
		catch (RDF4JException e) {
			System.out.println("Exception : " + e.toString());
		}  
		finally {
			conn.close();
		}
		return iri;
	}

	// School furniture
	static IRI createSchoolFurniture(Repository rep, String identifier, String name, float price, String subject, int studentAge, String material) {

		ValueFactory f = rep.getValueFactory();

		IRI iri = f.createIRI(LRI.NAMESPACE, identifier);

		RepositoryConnection conn = rep.getConnection();

		try {
			conn.add(iri, RDF.TYPE, LRI.SCHOOLFURNITURE);
			conn.add(iri, RDFS.LABEL, f.createLiteral(identifier, XMLSchema.STRING));
			conn.add(iri, RDFS.SUBCLASSOF, LRI.FURNITURE);
			conn.add(iri, LRI.PRICE, f.createLiteral(price));
			conn.add(iri, LRI.SUBJECT, f.createLiteral(subject, XMLSchema.STRING));
			conn.add(iri, LRI.NAME, f.createLiteral(name, XMLSchema.STRING));
			conn.add(iri, LRI.STUDENTAGE, f.createLiteral(studentAge));
			conn.add(iri, LRI.MATERIAL, f.createLiteral(material, XMLSchema.STRING));
		}	
		catch (RDF4JException e) {
			System.out.println("Exception : " + e.toString());
		}  
		finally {
			conn.close();
		}
		return iri;
	}

	// Furniture
	static IRI createFurniture(Repository rep, String identifier, String material, String name, float price) {

		ValueFactory f = rep.getValueFactory();

		IRI iri = f.createIRI(LRI.NAMESPACE, identifier);

		RepositoryConnection conn = rep.getConnection();

		try {
			conn.add(iri, RDF.TYPE, LRI.FURNITURE);
			conn.add(iri, RDFS.LABEL, f.createLiteral(identifier, XMLSchema.STRING));
			conn.add(iri, LRI.MATERIAL, f.createLiteral(material, XMLSchema.STRING));
			conn.add(iri, LRI.NAME, f.createLiteral(name, XMLSchema.STRING));
			conn.add(iri, LRI.PRICE, f.createLiteral(price));
		}	
		catch (RDF4JException e) {
			System.out.println("Exception : " + e.toString());
		}  
		finally {
			conn.close();
		}
		return iri;
	}

}
