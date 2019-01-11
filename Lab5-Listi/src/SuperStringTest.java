import java.util.*;
/*
class Test{

    public static void main(String[] args) {
        SuperString ss=new SuperString();
        ss.append("Janaki");
        ss.append("Filip");
        ss.append("Martin");
        ss.append("Petar");
        ss.reverse();
        System.out.println(ss.toString());
    }
}
*/
class SuperString {


    LinkedList<String> lista;
    ArrayList<Integer> cmds;
    SuperString(){
        lista=new LinkedList<>();
        cmds = new ArrayList<>();

    }

    void append(String s) {
        lista.addLast(s);
        cmds.add(0,0);
    }
    void insert(String s){
        lista.addFirst(s);
        cmds.add(0, 1);
    }
    boolean contains(String s){
        return toString().contains(s);

    }
    String reverseString(String s){
        StringBuilder sb=new StringBuilder(s);
        return sb.reverse().toString();
    }

    void reverse(){
        LinkedList<String> backwards=new LinkedList<>();
        int n=lista.size();
        for(int i=0;i<n;i++) {
            backwards.add(reverseString(lista.getLast()));
            lista.removeLast();

        }

        lista=backwards;

        for(int i=0; i<cmds.size();i++){
            if(cmds.get(i) == 0)cmds.set(i, 1);
            else if(cmds.get(i) == 1)cmds.set(i, 0);
        }


    }
    void removeLast(int k){

        for(int i=0;i<k;i++){
            int cmd = cmds.get(0);
            cmds.remove(0);
            if(cmd == 0){
                lista.removeLast();
            }else if(cmd == 1){
                lista.removeFirst();
            }
        }
    }


    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        for(String s:lista){
            sb.append(s);
        }
        return sb.toString();
    }
}
public class SuperStringTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (  k == 0 ) {
            SuperString s = new SuperString();
            while ( true ) {
                int command = jin.nextInt();
                if ( command == 0 ) {//append(String s)
                    s.append(jin.next());
                }
                if ( command == 1 ) {//insert(String s)
                    s.insert(jin.next());
                }
                if ( command == 2 ) {//contains(String s)
                    System.out.println(s.contains(jin.next()));
                }
                if ( command == 3 ) {//reverse()
                    s.reverse();
                }
                if ( command == 4 ) {//toString()
                    System.out.println(s);
                }
                if ( command == 5 ) {//removeLast(int k)
                    s.removeLast(jin.nextInt());
                }
                if ( command == 6 ) {//end
                    break;
                }
            }
        }
    }

}