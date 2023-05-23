package Database;

public class CheckerCheque {
    //long acc_no;
    static int result;
    /*BAYAN SERVICES
    Acc Length = 9
    Account Range = 700000000-799999999*/
    public int BayanChecker(long accNum){
        long new_accno = accNum / 10;
        long chk_digitVal = accNum % 10;
        long chk_digit = 0;
        int wt = 1;
        long rem = 0;
        //System.out.print(chk_digitVal + "\n\n");
        do{
            wt = wt * 2;
            rem = new_accno % 10;
            chk_digit = chk_digit + (rem * wt);
            new_accno = new_accno / 10;

            //System.out.print("WT: " + wt + "\nRem: " + rem + "\nCheck Digit: " + chk_digit + "\n\n");
        }
        while(new_accno > 0);

        chk_digit = chk_digit % 11;
        //System.out.print("\n\nCheck Digit mod 11: " + chk_digit + "\n");

        if(chk_digit > 9){
            //System.out.print(chk_digit + " is greater than 9");
            chk_digit = 0;
            result = -1;
        }
        else if(chk_digit < 9 && chk_digit != chk_digitVal){
            result = 0;
            //System.out.print(chk_digit + " is less than 9");
            //chk_digit = 0;
        }
        else if(chk_digit == chk_digitVal){
            result = 1;
        }
        return result;
    }

     /*INNOVE SERVICES*
    Acc Length = 9
    Account Range = 100000000-999999999/
     */
    public int InnoveChecker(long accNum){
        long new_accno = accNum / 10;
        long chk_digitVal = accNum % 10;
        long chk_digit = 0;
        int wt = 1;
        long rem = 0;
        do{
            wt = wt * 2;
            rem = new_accno % 10;
            chk_digit = chk_digit + (rem * wt);
            new_accno = new_accno / 10;

            //System.out.print("WT: " + wt + "\nRem: " + rem + "\nCheck Digit: " + chk_digit + "\n\n");
        }
        while(new_accno > 0);

        chk_digit = chk_digit % 11;

        if(chk_digit > 9){
            //System.out.print(chk_digit + " is greater than 9");
            chk_digit = 0;
            result = -1;
        }
        else if(chk_digit < 9 && chk_digit != chk_digitVal){
            result = 0;
            //System.out.print(chk_digit + " is less than 9");
            //chk_digit = 0;
        }
        else if(chk_digit == chk_digitVal){
            result = 1;
        }
        return result;
    }

     /*GLOBE HANDYPHONE SERVICES*
    Acc Length = 8
    Account Range = 10000000-99999999/
     */

    public int GlobeHandyphoneChecker(long accNum){
        long c = 0;
        long c_digitVal = accNum % 10;
        long wt = 1;
        long rem = 0;
        long new_acc = 10000000 + (accNum / 10);

        while (new_acc > 0){
            wt = wt + 1;
            rem = new_acc % 10;
            c = c + (rem * wt);
            new_acc = new_acc / 10;
        }

        c = c % 11;
        c = 11 - c;

        if(c == c_digitVal){
            result = 1;
        }
        else{
            result = -1;
        }
        return result;
    }

    /*FA ID FOR POSTPAID SERVICES*
    Acc Length = 9
    Generating 10 Digits FA ID/
     */

    public int FA_ID(String accNum){
        boolean isFinished = false;
        //int j[] = new int[accNum.length()];
        int k[] = new int[accNum.length()];
        long digitSum = 0;
        int counter = 0;
        long newAcc = 0;

        while(isFinished == false){
            //System.out.print("\nCURRENT ACCOUNT NUMER: " + accNum + "\n");
            char[] num = accNum.toCharArray();
            for(int i = 0; i < 9; i++){
                //System.out.print(k[i] + " ");
                k[i] = Character.getNumericValue(num[i]);
                //System.out.print(k[i] + " x " + (i + 1) + " = " + (k[i] * (i + 1)) + "\n");
                digitSum = digitSum + (k[i] * (i + 1));
            }
            long rem = digitSum % 11;
            if(rem < 10){
                if(accNum.length() == 9){
                    long chkSum = rem;
                    long verifySum = 0;
                    long verifyRem = 0;
                    for(int i = 0; i < 9; i++){
                        verifySum = verifySum + (k[i] * (i + 1));
                    }

                    verifyRem = verifySum % 11;

                    if(verifyRem < 10 && verifyRem == chkSum){
                        isFinished = true;
                        /*System.out.print("\n");
                        System.out.print("Digit Sum: " + digitSum + "\n");
                        //System.out.print("\n");
                        System.out.print("Rem: " + rem + "\n");
                        //System.out.print("\n");*/
                        accNum = accNum + rem;
                        //System.out.print("Accepted FA ID: " + accNum);
                        result = 1;
                    }
                    else{
                        //System.out.print("FA ID is not valid");
                        result = -1;
                    }
                }
                else{
                    //System.out.print("Must be 9 digits only");
                    result = 0;
                    break;
                }
            }
            else{
                newAcc = Long.parseLong(accNum);
                /*System.out.print("\n");
                System.out.print("Digit Sum: " + digitSum + "\n");
                System.out.print("Rem: " + rem + "\n");
                System.out.print("Rejected FA ID: " + newAcc + rem);
                System.out.print("\n\n");*/
                newAcc = newAcc + 1;
                accNum = Long.toString(newAcc);
                num = null;
                digitSum = 0;
                result = -1;
                //isFinished = true;
            }
        }
        return result;
    }
}
