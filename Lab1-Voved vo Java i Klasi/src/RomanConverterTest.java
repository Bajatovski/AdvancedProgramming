import java.util.Scanner;
import java.util.stream.IntStream;


public class RomanConverterTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        IntStream.range(0, n)
                .forEach(x -> System.out.println(RomanConverter.toRoman(scanner.nextInt())));
        scanner.close();
    }
}


class RomanConverter {
    /**
     * Roman to decimal converter
     *
     * @param n number in decimal format
     * @return string representation of the number in Roman numeral

     */
    public RomanConverter() {
    }

    public static String toRoman(int n) {
        final int [] niza= {1000,900,500,400,100,90,50,40,10,9,5,4,1};
        final String [] znaci={"M","CM","D","CD","C","XC","L","XL","X","IX","V","IV","I"};
        int i=0;

        StringBuilder a=new StringBuilder("");
        while(n>0){
            int k=n/niza[i];

            for(int j=0;j<k;j++){
                a.append(znaci[i]);
            }
            n=n-(k*niza[i]);
            i++;
        }

        return a.toString();
    }

}
