public class Utilities {

    // boolean function to check whether the given string parameter is a valid integer >= 0 or not
    public boolean isPositiveInt(String input){
        if (input == null || input.length() == 0)
            return false;
        try{
            int val = Integer.parseInt(input);
            return val >= 0;
        }catch (Exception ignored){
            return false;
        }
    }

}
