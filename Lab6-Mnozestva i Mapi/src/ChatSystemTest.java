import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

    public class ChatSystemTest {

        public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchRoomException {
            Scanner jin = new Scanner(System.in);
            int k = jin.nextInt();
            if (k == 0) {
                ChatRoom cr = new ChatRoom(jin.next());
                int n = jin.nextInt();
                for (int i = 0; i < n; ++i) {
                    k = jin.nextInt();
                    if (k == 0) cr.addUser(jin.next());
                    if (k == 1) cr.removeUser(jin.next());
                    if (k == 2) System.out.println(cr.hasUser(jin.next()));
                }
                System.out.println("");
                System.out.println(cr.toString());
                n = jin.nextInt();
                if (n == 0) return;
                ChatRoom cr2 = new ChatRoom(jin.next());
                for (int i = 0; i < n; ++i) {
                    k = jin.nextInt();
                    if (k == 0) cr2.addUser(jin.next());
                    if (k == 1) cr2.removeUser(jin.next());
                    if (k == 2) cr2.hasUser(jin.next());
                }
                System.out.println(cr2.toString());
            }
            if (k == 1) {
                ChatSystem cs = new ChatSystem();
                Method mts[] = cs.getClass().getDeclaredMethods();
                while (true) {
                    String cmd = jin.next();
                    if (cmd.equals("stop")) break;
                    if (cmd.equals("print")) {
                        System.out.println(cs.getRoom(jin.next()) + "\n");
                        continue;
                    }
                    for (Method m : mts) {
                        if (m.getName().equals(cmd)) {
                            String params[] = new String[m.getParameterTypes().length];
                            for (int i = 0; i < params.length; ++i) params[i] = jin.next();
                            m.invoke(cs, params);
                        }
                    }
                }
            }
        }

    }
    class NoSuchRoomException extends RuntimeException{
        NoSuchRoomException( String roomName){
            super(roomName);
        }
    }
    class NoSuchUserException extends  RuntimeException{
        NoSuchUserException(String userName){
            super(userName);
        }
    }
    class ChatRoom{

        String name;
        Set<String> users;
        ChatRoom(String name){
            this.name=name;
            users=new TreeSet<>();
        }
        void addUser(String username){
            users.add(username);
        }
        void removeUser(String username){
            users.remove(username);
        }

        @Override
        public String toString() {
            StringBuilder sb=new StringBuilder();
            sb.append(name).append("\n");
            if(users.isEmpty()){
                sb.append("EMPTY").append("\n");
            }
            else
                for(String s:users){
                    sb.append(s).append("\n");
                }
            return sb.toString();
        }
        boolean hasUser(String username){
            return users.contains(username);
        }
        int numUsers(){
            return users.size();
        }

    }

    class ChatSystem {
        Map<String,ChatRoom> mapa;
        Set<String> users;

        ChatSystem(){
            mapa=new TreeMap<>();
            users=new TreeSet<>();
        }
        void addRoom(String roomName){
            mapa.put(roomName,new ChatRoom(roomName));
        }
        void removeRoom(String roomName){
            mapa.remove(roomName);
        }
        ChatRoom getRoom(String roomName){
            if(!mapa.containsKey(roomName)) throw new NoSuchRoomException(roomName);
            return mapa.get(roomName);

        }


        void registerAndJoin(String userName, String roomName){
            users.add(userName);
            if(mapa.containsKey(roomName)){
                mapa.get(roomName).addUser(userName);
            }
        }

        void joinRoom(String userName, String roomName){
            if(!mapa.containsKey(roomName)) throw new NoSuchRoomException(roomName);
            if(!users.contains(userName)) throw new NoSuchUserException(userName);
            users.add(userName);
            mapa.get(roomName).addUser(userName);

        }
        void leaveRoom(String username, String roomName){
            if(!mapa.containsKey(roomName)) throw new NoSuchUserException(roomName);
            if(!users.contains(username)) throw new NoSuchUserException(username);
            mapa.get(roomName).removeUser(username);
        }
        void followFriend(String username, String friend_username){
            if(!users.contains(friend_username)) throw new NoSuchUserException(friend_username);
            if(!users.contains(username)) throw new NoSuchUserException(username);
            for(ChatRoom s:mapa.values()){
                if(s.hasUser(friend_username)){
                    s.addUser(username);
                }
            }

        }


        void joinToRoomWithLowestNumUsers(String userName) {
            if (mapa.size() == 0) return;
            int lowestNum  = mapa.values().stream().mapToInt(ChatRoom::numUsers).min().getAsInt();
            ChatRoom cr = mapa.values().stream().filter(a-> a.numUsers() == lowestNum).findFirst().get();
            cr.addUser(userName);
        }

        void register(String userName) {
        users.add(userName);
            joinToRoomWithLowestNumUsers(userName);

        }




    }



