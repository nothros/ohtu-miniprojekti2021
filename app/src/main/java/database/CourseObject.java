package database;

public class CourseObject {	
	private int id;
    private String name;
    private String department;

    public CourseObject(String name, String department) {
        this.name = name;
        this.department = department;
    }
    
    public CourseObject(int id, String name, String department) {
    	this.id = id;
        this.name = name;
        this.department = department;
    }

    @Override
    public String toString() {
        return "{" +
            " name='" + getName() + "'" +
            ", department='" + getDepartment() + "'}";
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

}
