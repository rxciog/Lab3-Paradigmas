package namedEntities;

public class Person {
    private String name;
    private int age;
    private String pob;

    public Person(String name, int age, String pob) {
        this.name = name;
        this.age = age;
        this.pob = pob;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String  getPOB() {
        return pob;
    }
}
