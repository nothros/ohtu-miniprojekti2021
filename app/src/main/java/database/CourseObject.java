package database;

public class CourseObject {	
	private int id;
    private String name;
    private String department;
    private String description;

    public CourseObject(String name) {
        this.name = name;
    }
    
    public CourseObject(int id, String name) {
    	this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "{name='" + getName() + "'}";
    }

    public int getId(){
    	return this.id;
	}

    public void setId(int id) {
        this.id = id;
    }
    
    public String getName(){
    	return this.name;
	}

    public void setName(String name) {
        this.name = name;
    }
	    
    public String getDepartment(){
        return this.department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
    
    public String getDescription(){
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}