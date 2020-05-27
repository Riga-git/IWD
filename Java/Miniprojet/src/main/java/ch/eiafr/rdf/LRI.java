package ch.eiafr.rdf;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;

public class LRI {
	
	protected static String NAMESPACE = "http:/lri.ch/";
	
	// Classes
	protected static IRI ADDRESS;
	protected static IRI PROF;
	protected static IRI STUDENT;
	protected static IRI SCHOOL;
	protected static IRI COURSE;
	protected static IRI PUBLICSERVICE;
	protected static IRI BUILDING;
	protected static IRI CLASSROOM;
	protected static IRI SCHOOLFURNITURE;
	protected static IRI ROOM;
	protected static IRI FURNITURE;
	
	// Object properties
	protected static IRI LOCATED;
	protected static IRI WORKSFOR;
	protected static IRI TEACHIN;
	protected static IRI INCHARGEOF;
	protected static IRI FOLLOWS;
	protected static IRI LEARNSIN;
	protected static IRI HASCLASSROOM;
	protected static IRI LIVES;
	protected static IRI HASSCHOOLFURNITURE;
	protected static IRI HASFURNITURES;
	protected static IRI CONSTITUED;
	protected static IRI TAKEPLACEIN;
	protected static IRI HASCOURSE;
	
	// Data properties
	protected static IRI CAP;
	protected static IRI CITY;
	protected static IRI ROAD;
	protected static IRI SPECIALIZATION;
	protected static IRI REGISRATIONDATE;
	protected static IRI LEVEL;
	protected static IRI LANGUAGE;
	protected static IRI NAME;
	protected static IRI SCHOOLTYPE;
	protected static IRI OPENINGHOURS;
	protected static IRI SERVICETYPE;
	protected static IRI COLOR;
	protected static IRI HEIGHT;
	protected static IRI NUMBER;
	protected static IRI CAPACITY;
	protected static IRI STUDENTAGE;
	protected static IRI SUBJECT;
	protected static IRI SURFACE;
	protected static IRI VOLUME;
	protected static IRI MATERIAL;
	protected static IRI PRICE;

	
	public static void createIri() {
		
		ValueFactory vf = SimpleValueFactory.getInstance();
		
		// Classes
		ADDRESS = vf.createIRI(NAMESPACE, "Address");
		PROF = vf.createIRI(NAMESPACE, "Prof");
		STUDENT = vf.createIRI(NAMESPACE, "Student");
		SCHOOL = vf.createIRI(NAMESPACE, "Course");
		PUBLICSERVICE = vf.createIRI(NAMESPACE, "PublicService");
        BUILDING = vf.createIRI(NAMESPACE, "Building");          
        CLASSROOM = vf.createIRI(NAMESPACE, "ClassRoom");         
        SCHOOLFURNITURE = vf.createIRI(NAMESPACE, "SchoolFurniture");  
        ROOM = vf.createIRI(NAMESPACE, "Room");
        FURNITURE = vf.createIRI(NAMESPACE, "Furniture"); 
		
		// Object properties
        LOCATED = vf.createIRI(NAMESPACE, "located");           
        WORKSFOR = vf.createIRI(NAMESPACE, "worksFor");          
        TEACHIN = vf.createIRI(NAMESPACE, "teachIn");           
        INCHARGEOF = vf.createIRI(NAMESPACE, "inChargeOf");       
        FOLLOWS = vf.createIRI(NAMESPACE, "follows");          
        LEARNSIN = vf.createIRI(NAMESPACE, "learnsIn");        
        HASCLASSROOM = vf.createIRI(NAMESPACE, "hasClassRoom");      
        LIVES = vf.createIRI(NAMESPACE, "lives");           
        HASSCHOOLFURNITURE = vf.createIRI(NAMESPACE, "hasSchoolfurniture");
        HASFURNITURES = vf.createIRI(NAMESPACE, "hasFurnitures");     
        CONSTITUED = vf.createIRI(NAMESPACE, "constitued"); 
        TAKEPLACEIN = vf.createIRI(NAMESPACE, "takePalceIn"); 
        HASCOURSE = vf.createIRI(NAMESPACE, "hasCourse"); 
        
		// Data properties
		CAP = vf.createIRI(NAMESPACE, "cap");
		CITY = vf.createIRI(NAMESPACE, "city");
		ROAD = vf.createIRI(NAMESPACE, "road");	
		SPECIALIZATION = vf.createIRI(NAMESPACE, "spacialization");
		REGISRATIONDATE = vf.createIRI(NAMESPACE, "registrationDate");
		COURSE = vf.createIRI(NAMESPACE, "Course");
		LEVEL = vf.createIRI(NAMESPACE, "level");
		LANGUAGE = vf.createIRI(NAMESPACE, "language");
		NAME = vf.createIRI(NAMESPACE, "name");
		SCHOOLTYPE = vf.createIRI(NAMESPACE, "schoolType");
        OPENINGHOURS = vf.createIRI(NAMESPACE, "openingHours");      
        SERVICETYPE = vf.createIRI(NAMESPACE, "serviceType");       
        COLOR = vf.createIRI(NAMESPACE, "color");             
        HEIGHT = vf.createIRI(NAMESPACE, "height");            
        NUMBER = vf.createIRI(NAMESPACE, "number");            
        CAPACITY = vf.createIRI(NAMESPACE, "capacity");        
        STUDENTAGE = vf.createIRI(NAMESPACE, "studentAge");        
        SUBJECT = vf.createIRI(NAMESPACE, "subject");           
        SURFACE = vf.createIRI(NAMESPACE, "surface");           
        VOLUME = vf.createIRI(NAMESPACE, "volume");           
        MATERIAL = vf.createIRI(NAMESPACE, "material");          
        PRICE = vf.createIRI(NAMESPACE, "price");     
        
	}
}