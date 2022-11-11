public class Utilities {

    public boolean isPositiveInt(String input){
        if (input == null || input.length() == 0)
            return false;
        try{
            int val = Integer.parseInt(input);
            return val > 0;
        }catch (Exception ignored){
            return false;
        }
    }

}
