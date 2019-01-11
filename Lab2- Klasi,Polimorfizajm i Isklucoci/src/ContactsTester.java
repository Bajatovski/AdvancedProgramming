import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;


public class ContactsTester {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        Faculty faculty = null;

        int rvalue = 0;
        long rindex = -1;

        DecimalFormat df = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            rvalue++;
            String operation = scanner.next();

            switch (operation) {
                case "CREATE_FACULTY": {
                    String name = scanner.nextLine().trim();
                    int N = scanner.nextInt();

                    Student[] students = new Student[N];

                    for (int i = 0; i < N; i++) {
                        rvalue++;

                        String firstName = scanner.next();
                        String lastName = scanner.next();
                        String city = scanner.next();
                        int age = scanner.nextInt();
                        long index = scanner.nextLong();

                        if ((rindex == -1) || (rvalue % 13 == 0))
                            rindex = index;

                        Student student = new Student(firstName, lastName, city,
                                age, index);
                        students[i] = student;
                    }

                    faculty = new Faculty(name, students);
                    break;
                }

                case "ADD_EMAIL_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String email = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addEmailContact(date, email);
                    break;
                }

                case "ADD_PHONE_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String phone = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addPhoneContact(date, phone);
                    break;
                }

                case "CHECK_SIMPLE": {
                    System.out.println("Average number of contacts: "
                            + df.format(faculty.getAverageNumberOfContacts()));

                    rvalue++;

                    String city = faculty.getStudent(rindex).getCity();
                    System.out.println("Number of students from " + city + ": "
                            + faculty.countStudentsFromCity(city));

                    break;
                }

                case "CHECK_DATES": {

                    rvalue++;

                    System.out.print("Latest contact: ");
                    Contact latestContact = faculty.getStudent(rindex)
                            .getLatestContact();
                    if (latestContact.getType().equals("Email"))
                        System.out.println(((EmailContact) latestContact)
                                .getEmail());
                    if (latestContact.getType().equals("Phone"))
                        System.out.println(((PhoneContact) latestContact)
                                .getPhone()
                                + " ("
                                + ((PhoneContact) latestContact).getOperator()
                                .toString() + ")");

                    if (faculty.getStudent(rindex).getEmailContacts().length > 0&&faculty.getStudent(rindex).getPhoneContacts().length > 0) {
                        System.out.print("Number of email and phone contacts: ");
                        System.out
                                .println(faculty.getStudent(rindex)
                                        .getEmailContacts().length
                                        + " "
                                        + faculty.getStudent(rindex)
                                        .getPhoneContacts().length);

                        System.out.print("Comparing dates: ");
                        int posEmail = rvalue
                                % faculty.getStudent(rindex).getEmailContacts().length;
                        int posPhone = rvalue
                                % faculty.getStudent(rindex).getPhoneContacts().length;

                        System.out.println(faculty.getStudent(rindex)
                                .getEmailContacts()[posEmail].isNewerThan(faculty
                                .getStudent(rindex).getPhoneContacts()[posPhone]));
                    }

                    break;
                }

                case "PRINT_FACULTY_METHODS": {
                    System.out.println("Faculty: " + faculty.toString());
                    System.out.println("Student with most contacts: "
                            + faculty.getStudentWithMostContacts().toString());
                    break;
                }

            }

        }

        scanner.close();
    }
}
abstract class Contact {

    int god,mesec,den;

    Contact(String date)
    {
        String [] a=date.split("-");
        god=Integer.parseInt(a[0]);
        mesec=Integer.parseInt(a[1]);
        den=Integer.parseInt(a[2]);


    }

    boolean isNewerThan(Contact c){

        if(god>c.god) return true;
        else if(god==c.god) {
            if (mesec > c.mesec) return true;
            else if(mesec==c.mesec) {
                if (den > c.den) return true;
            }
        }

        return false;
    }
    abstract String getType();


}
class EmailContact extends Contact{

    private  String email;
    EmailContact(String date,String email) {
        super(date);
        this.email=email;
    }

    @Override
    String getType() {
        return "Email";
    }

    String getEmail(){
        return this.email;
    }

    @Override
    public String toString() {
        return "\""+email+"\"";
    }
}
class PhoneContact  extends Contact{
    enum Operator { VIP, ONE, TMOBILE }

    Operator operator;
    private String phone;
    PhoneContact(String date,String phone) {
        super(date);
        this.phone=phone;

    }

    @Override
    public String toString() {
        return "\""+phone+"\"";
    }

    String getPhone(){
        return this.phone;
    }
    Operator getOperator(){

        char karakter=phone.charAt(2);
        if(karakter=='0'||karakter=='1'||karakter=='2') return Operator.TMOBILE;
        if(karakter=='5'||karakter=='6') return Operator.ONE;
        return Operator.VIP;


    }

    @Override
    String getType() {
        return "Phone";
    }
}
class Student {
    private String firstName;
    private String lastName;
    private String city;
    private int age;
    private long index;
    ArrayList<Contact> contacts;
    Student(String firstName, String lastName, String city, int age, long index){

        this.firstName=firstName;
        this.lastName=lastName;
        this.city=city;
        this.age=age;
        this.index=index;
        contacts=new ArrayList<>();



    }
    void addEmailContact(String date, String email){
        contacts.add(new EmailContact(date,email));
    }
    void addPhoneContact(String date,String phone){
        contacts.add(new PhoneContact(date,phone));
    }

    Contact [] getEmailContacts(){
        return contacts.stream().filter(a->a.getType().equals("Email")).toArray(EmailContact[]::new);
    }
    Contact [] getPhoneContacts(){
        return contacts.stream().filter(a->a.getType().equals("Phone")).toArray(PhoneContact[]::new);
    }
    public String getCity(){
        return this.city;
    }
    public int getNumContacts(){
        return contacts.size();
    }
    public String getFullName(){
        return this.firstName+" "+this.lastName;
    }
    public long getIndex(){
        return index;
    }
    Contact getLatestContact(){

        return contacts.stream().reduce((a,b)->(a.isNewerThan(b)) ? a : b).get();
    }

    @Override
    public String toString() {
        //return String.format("ime:%s, prezime:%s, vozrast:%d, indeks:%d, telefonskiKontakti:%s,emailKontakti:%s" ,firstName,lastName,age,city,index,getPhoneContacts(),getEmailContacts());

        return String.format("{\"ime\":\"%s\", \"prezime\":\"%s\", " +
                        "\"vozrast\":%d, \"grad\":\"%s\", \"indeks\":%d, \"telefonskiKontakti\"" +
                        ":%s, \"emailKontakti\":%s}", firstName, lastName, age, city, index,
                Arrays.asList(getPhoneContacts()), Arrays.asList(getEmailContacts()));
    }

}
class Faculty {
    private String name;
    private ArrayList<Student> students;
    Faculty(String name, Student [] students){
        this.name=name;
        this.students=new ArrayList<>();
        // this.students.addAll(Arrays.stream(students).collect(Collectors.toList()));
        for(Student s:students){
            this.students.add(s);
        }

    }
    int countStudentsFromCity(String cityName){
        return (int) students.stream().filter(a->a.getCity().equals(cityName)).count();
    }
    Student getStudent(long index){
        return students.stream().filter(a->a.getIndex()==index).findFirst().get();
    }
    double getAverageNumberOfContacts(){

        return students.stream().mapToInt(a->a.getNumContacts()).sum()/(double)students.size();
    }
    Student getStudentWithMostContacts(){

        return students.stream().reduce((a,b)-> (a.getNumContacts()>b.getNumContacts() ? a :(a.getNumContacts()==
                b.getNumContacts() ? (a.getIndex()>b.getIndex() ? a:b):b))).get();
    }

    @Override
    public String toString() {
        String studentsString=students.toString();
        return String.format("{\"fakultet\":\"%s\", \"studenti\":%s}",name,studentsString);
    }
}

