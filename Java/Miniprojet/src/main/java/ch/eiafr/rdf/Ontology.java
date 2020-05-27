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
		RepositoryConnection conn = rep.getConnection();
		ValueFactory f = rep.getValueFactory();

		try {
			iriMap = createIndividuals(conn, f);
			buildOntology(conn);
			
			RepositoryResult<Statement> statements = conn.getStatements(null, null, null, true);
			
			Model model = Iterations.addAll(statements, new LinkedHashModel());
			model.setNamespace("rdf", RDF.NAMESPACE);
			model.setNamespace("rdfs", RDFS.NAMESPACE);
			model.setNamespace("xsd", XMLSchema.NAMESPACE);
			model.setNamespace("foaf", FOAF.NAMESPACE);
			model.setNamespace("lri", LRI.NAMESPACE);
			
			Rio.write(model, System.out, RDFFormat.TURTLE);
				
		}
		catch (RDF4JException e) {
			System.out.println("Exception : " + e.toString());
		}  
		finally {
			conn.close();
		}

	}
	
	static void buildOntology(RepositoryConnection conn) {
		
		// RDFS
		conn.add(FOAF.PERSON, LRI.WORKSFOR, LRI.PUBLICSERVICE);
		conn.add(LRI.PROF, RDFS.SUBCLASSOF, FOAF.PERSON);
		conn.add(LRI.STUDENT, RDFS.SUBCLASSOF, FOAF.PERSON);
		conn.add(LRI.PROF, LRI.INCHARGEOF, LRI.COURSE);
		conn.add(LRI.PROF, LRI.LIVES, LRI.ADDRESS);
		conn.add(LRI.PROF, LRI.TEACHIN, LRI.SCHOOL);
		conn.add(LRI.STUDENT, LRI.FOLLOWS, LRI.COURSE);
		conn.add(LRI.STUDENT, LRI.LEARNSIN, LRI.CLASSROOM);
		conn.add(LRI.STUDENT, LRI.LIVES, LRI.ADDRESS);
		conn.add(LRI.COURSE, LRI.TAKEPLACEIN, LRI.CLASSROOM);
		conn.add(LRI.SCHOOL, LRI.HASCOURSE, LRI.COURSE);
		conn.add(LRI.SCHOOL, LRI.HASCLASSROOM, LRI.HASCLASSROOM);
		conn.add(LRI.SCHOOL, RDFS.SUBCLASSOF, LRI.BUILDING);
		conn.add(LRI.SCHOOL, RDFS.SUBCLASSOF, LRI.PUBLICSERVICE);
		conn.add(LRI.TEACHIN, RDFS.SUBPROPERTYOF, LRI.WORKSFOR);
		conn.add(LRI.BUILDING, LRI.LOCATED, LRI.ADDRESS);
		conn.add(LRI.BUILDING, LRI.CONSTITUED, LRI.ROOM);
		conn.add(LRI.CLASSROOM, LRI.HASSCHOOLFURNITURE, LRI.SCHOOLFURNITURE);
		conn.add(LRI.CLASSROOM, RDFS.SUBCLASSOF, LRI.ROOM);
		conn.add(LRI.HASCLASSROOM, RDFS.SUBPROPERTYOF, LRI.CONSTITUED);
		conn.add(LRI.SCHOOLFURNITURE, RDFS.SUBCLASSOF, LRI.FURNITURE);
		conn.add(LRI.HASSCHOOLFURNITURE, RDFS.SUBPROPERTYOF, LRI.HASFURNITURES);
		conn.add(LRI.ROOM, LRI.HASFURNITURES, LRI.FURNITURE);
		
		// RDF
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

	}

	static Map<String, IRI> createIndividuals(RepositoryConnection conn, ValueFactory f) {
		// Examples
		
		Map<String, IRI> map = new HashMap<String, IRI>();
		map.put("jerome", createStudent(conn, f, "Jerome", "Jerôme Garo", "12.03"));
		map.put("desire", createStudent(conn, f, "Desire", "Desire Nonis", "12.03"));
		map.put("luca", createProf(conn, f, "Luca", "Luca Rigazzi", "German"));
		map.put("julien", createProf(conn, f, "Julien", "Julien Tscherig", "French"));
		map.put("inlinguo", createSchool(conn, f, "Inlinguo", "Inlinguo", "Language School"));
		map.put("germanCourse", createCourse(conn, f, "GermanCourse", "B2", "German"));
		map.put("frenchCourse", createCourse(conn, f, "FrenchCourse", "C1", "FrenchS"));
		
		map.put("room_132", createClassRoom(conn, f, "Room_132", 132, 32));
		map.put("room_256", createClassRoom(conn, f, "Room_256", 256, 12));
		map.put("whiteBoard", createSchoolFurniture(conn, f, "WhiteBoard", "White Board", 250 , "Math class"));
		map.put("table", createSchoolFurniture(conn, f, "Table", "Table", 320, "Chemistry", 20, "Wood"));
		map.put("schooAddress", createAddress(conn, f, "SchooAddress", 1400, "Yverdon", "Rue des langues 12"));
		map.put("lucaAddress", createAddress(conn, f, "LucaAddress", 1422, "Grandson", "Rue des Jardins 22"));							
		map.put("julienAddress", createAddress(conn, f, "JulienAddress", 1212, "Lorem", "Ipsum 45"));		
		map.put("desireAddress", createAddress(conn, f, "DesireAddress", 3232, "Nunningen", "Lebernweg 5"));		
		map.put("jeromeAddress", createAddress(conn, f, "JeromeAddress", 3206, "Gals", "Bern Strasse 1"));		
		
		return map;
	}

	// Address
	static IRI createAddress(RepositoryConnection conn, ValueFactory f, String identifier, int cap, String city, String Road) {

		IRI iri = f.createIRI(LRI.NAMESPACE, identifier);

		conn.add(iri, RDF.TYPE, LRI.ADDRESS);
		conn.add(iri, RDFS.LABEL, f.createLiteral(identifier, XMLSchema.STRING));
		conn.add(iri, LRI.CAP, f.createLiteral(cap));
		conn.add(iri, LRI.CITY, f.createLiteral(city, XMLSchema.STRING));
		conn.add(iri, LRI.ROAD, f.createLiteral(Road, XMLSchema.STRING));

		return iri;
	}

	// Prof
	static IRI createProf(RepositoryConnection conn, ValueFactory f, String identifier, String name, String specialization) {

		IRI iri = f.createIRI(LRI.NAMESPACE, identifier);

		conn.add(iri, RDF.TYPE, LRI.PROF);
		conn.add(iri, RDFS.LABEL, f.createLiteral(identifier, XMLSchema.STRING));
		conn.add(iri, FOAF.NAME, f.createLiteral(name, XMLSchema.STRING));
		conn.add(iri, LRI.SPECIALIZATION, f.createLiteral(name, XMLSchema.STRING));

		return iri;
	}

	// Student
	static IRI createStudent(RepositoryConnection conn, ValueFactory f, String identifier, String name, String registrationDate) {

		IRI iri = f.createIRI(LRI.NAMESPACE, identifier);

		conn.add(iri, RDF.TYPE, LRI.STUDENT);
		conn.add(iri, RDFS.LABEL, f.createLiteral(identifier, XMLSchema.STRING));
		conn.add(iri, FOAF.NAME, f.createLiteral(name, XMLSchema.STRING));
		conn.add(iri, LRI.REGISRATIONDATE, f.createLiteral(registrationDate, XMLSchema.STRING));

		return iri;
	}

	// Course
	static IRI createCourse(RepositoryConnection conn, ValueFactory f, String identifier, String level, String language) {

		IRI iri = f.createIRI(LRI.NAMESPACE, identifier);

		conn.add(iri, RDF.TYPE, LRI.COURSE);
		conn.add(iri, RDFS.LABEL, f.createLiteral(identifier, XMLSchema.STRING));
		conn.add(iri, LRI.LEVEL, f.createLiteral(level, XMLSchema.STRING));
		conn.add(iri, LRI.LANGUAGE, f.createLiteral(language, XMLSchema.STRING));

		return iri;
	}

	// Public school
	static IRI createSchool(RepositoryConnection conn, ValueFactory f, String identifier, String Name, String schoolType) {

		IRI iri = f.createIRI(LRI.NAMESPACE, identifier);

		conn.add(iri, RDF.TYPE, LRI.SCHOOL);
		conn.add(iri, RDFS.LABEL, f.createLiteral(identifier, XMLSchema.STRING));
		conn.add(iri, LRI.NAME, f.createLiteral(Name, XMLSchema.STRING));
		conn.add(iri, LRI.SCHOOLTYPE, f.createLiteral(schoolType, XMLSchema.STRING));

		return iri;
	}

	// Public service
	static IRI createPublicService(RepositoryConnection conn, ValueFactory f, String identifier, String name, String schoolType) {

		IRI iri = f.createIRI(LRI.NAMESPACE, identifier);

		conn.add(iri, RDF.TYPE, LRI.PUBLICSERVICE);
		conn.add(iri, RDFS.LABEL, f.createLiteral(identifier, XMLSchema.STRING));
		conn.add(iri, LRI.SERVICETYPE, f.createLiteral(name, XMLSchema.STRING));
		conn.add(iri, LRI.OPENINGHOURS, f.createLiteral(schoolType, XMLSchema.STRING));

		return iri;
	}

	// Class room
	static IRI createClassRoom(RepositoryConnection conn, ValueFactory f, String identifier, int number, int capacity) {

		IRI iri = f.createIRI(LRI.NAMESPACE, identifier);

		conn.add(iri, RDF.TYPE, LRI.CLASSROOM);
		conn.add(iri, RDFS.LABEL, f.createLiteral(identifier, XMLSchema.STRING));
		conn.add(iri, LRI.CAPACITY, f.createLiteral(number));
		conn.add(iri, LRI.NUMBER, f.createLiteral(capacity));

		return iri;
	}

	// Class Building
	static IRI createBuilding(RepositoryConnection conn, ValueFactory f, String identifier, String color, int height) {

		IRI iri = f.createIRI(LRI.NAMESPACE, identifier);
		
		conn.add(iri, RDF.TYPE, LRI.BUILDING);
		conn.add(iri, RDFS.LABEL, f.createLiteral(identifier, XMLSchema.STRING));
		conn.add(iri, LRI.COLOR, f.createLiteral(color, XMLSchema.STRING));
		conn.add(iri, LRI.HEIGHT, f.createLiteral(height));

		return iri;
	}

	// Room
	static IRI createRoom(RepositoryConnection conn, ValueFactory f, String identifier, int surface, int volume) {

		IRI iri = f.createIRI(LRI.NAMESPACE, identifier);

		conn.add(iri, RDF.TYPE, LRI.ROOM);
		conn.add(iri, RDFS.LABEL, f.createLiteral(identifier, XMLSchema.STRING));
		conn.add(iri, LRI.SURFACE, f.createLiteral(surface));
		conn.add(iri, LRI.VOLUME, f.createLiteral(volume));

		return iri;
	}

	// School furniture
	static IRI createSchoolFurniture(RepositoryConnection conn, ValueFactory f, String identifier, String name, float price, String subject) {

		IRI iri = f.createIRI(LRI.NAMESPACE, identifier);

		conn.add(iri, RDF.TYPE, LRI.SCHOOLFURNITURE);
		conn.add(iri, RDFS.LABEL, f.createLiteral(identifier, XMLSchema.STRING));
		conn.add(iri, LRI.PRICE, f.createLiteral(price));
		conn.add(iri, LRI.SUBJECT, f.createLiteral(subject, XMLSchema.STRING));
		conn.add(iri, LRI.SUBJECT, f.createLiteral(name, XMLSchema.STRING));

		return iri;
	}

	// School furniture
	static IRI createSchoolFurniture(RepositoryConnection conn, ValueFactory f, String identifier, String name, float price, String subject, int studentAge, String material) {

		IRI iri = f.createIRI(LRI.NAMESPACE, identifier);

		conn.add(iri, RDF.TYPE, LRI.SCHOOLFURNITURE);
		conn.add(iri, RDFS.LABEL, f.createLiteral(identifier, XMLSchema.STRING));
		conn.add(iri, LRI.PRICE, f.createLiteral(price));
		conn.add(iri, LRI.SUBJECT, f.createLiteral(subject, XMLSchema.STRING));
		conn.add(iri, LRI.NAME, f.createLiteral(name, XMLSchema.STRING));
		conn.add(iri, LRI.STUDENTAGE, f.createLiteral(studentAge));
		conn.add(iri, LRI.MATERIAL, f.createLiteral(material, XMLSchema.STRING));
		
		return iri;
	}

	// Furniture
	static IRI createFurniture(RepositoryConnection conn, ValueFactory f, String identifier, String material, String name, float price) {

		IRI iri = f.createIRI(LRI.NAMESPACE, identifier);

		conn.add(iri, RDF.TYPE, LRI.FURNITURE);
		conn.add(iri, RDFS.LABEL, f.createLiteral(identifier, XMLSchema.STRING));
		conn.add(iri, LRI.MATERIAL, f.createLiteral(material, XMLSchema.STRING));
		conn.add(iri, LRI.NAME, f.createLiteral(name, XMLSchema.STRING));
		conn.add(iri, LRI.PRICE, f.createLiteral(price));

		return iri;
	}

}
