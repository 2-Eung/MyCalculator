package com.example.MyCalculator.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class CalculatorService {

    public String deleteInputPanels(String inputPanels) {
        String beforeInputPanels = "";
        for (int i = 0; i < inputPanels.length() - 1; i++) {
            char inputPanel = inputPanels.charAt(i);
            beforeInputPanels += inputPanel;
        }
        return beforeInputPanels;
    }

    public double baseCalculate(char oper, double num1, double num2) {
        if (oper == '+') { return num1 + num2; }
        if (oper == '-') { return num1 - num2; }
        if (oper == '*') { return num1 * num2; }
        if (oper == '/') {
            if (num2 == 0) {
                throw new IllegalStateException(notGoodInputPanels(0));
            }
            return num1 / num2;
        }
        return 0;
    }   // 기본적인 계산식

    public String notGoodInputPanels(int errorNumber){
        switch (errorNumber) {
            case 0 : return "계산식이 올바르지 않습니다. 0 으로 나눌수 없어요.";
            case 1 : return "계산식이 올바르지 않습니다. 빈칸은 안돼요.";
            case 2 : return "계산식이 올바르지 않습니다. 처음에 + , * , / , ) , . 이 오면 안돼요.";
            case 3 : return "계산식이 올바르지 않습니다. 부호가 중첩되었거나 괄호와 부호의 조합이 옳지않습니다.";
            case 4 : return "계산식이 올바르지 않습니다. 소수점이 잘못되었습니다.";
            case 5 : return "계산식이 올바르지 않습니다. 괄호의 쌍이 맞지 않습니다.";
            case 6 : return "계산식이 올바르지 않습니다. 마지막에 + , - , * , / , ( , . 이 오면 안돼요.";
            case 99 : return "너 뭐하는녀석이야";
        }
        return "";
        // 99 : 너 뭐하는녀석이야
        // 0 : 계산식이 올바르지 않습니다. 0 으로 나눌수 없어요.
        // 1 : 계산식이 올바르지 않습니다. 빈칸은 안돼요.
        // 2 : 계산식이 올바르지 않습니다. 처음에 + , * , / , ) , . 이 오면 안돼요.
        // 3 : 계산식이 올바르지 않습니다. 부호가 중첩되었거나 괄호와 부호의 조합이 옳지않습니다.
        // 4 : 계산식이 올바르지 않습니다. 소수점이 잘못되었습니다.
        // 5 : 계산식이 올바르지 않습니다. 괄호의 쌍이 맞지 않습니다.
        // 6 : 계산식이 올바르지 않습니다. 마지막에 + , - , * , / , ( , . 이 오면 안돼요.
    }   // 에러나는곳에 넣고 에러종류 파악하기 그리고 같은 예외 던지기 throw new IllegalStateException();

    public int checkInputPanels(String inputPanels) {
        int count1 = 0;
        int count2 = 0;
        int bracketCount;

        // 99 : 너 뭐하는녀석이야 (입력할 수 없는값이 입력됨)
        for (int i = 0; i < inputPanels.length(); i++) {
            char inputPanel = inputPanels.charAt(i);
            if (!((48 <= inputPanel) && (inputPanel <= 57))
                    && inputPanel != '+'
                    && inputPanel != '-'
                    && inputPanel != '*'
                    && inputPanel != '/'
                    && inputPanel != '('
                    && inputPanel != ')'
                    && inputPanel != '.') {
                throw new IllegalStateException(notGoodInputPanels(99));
            }
        }

        // 1 : 계산식이 올바르지 않습니다. 빈칸은 안돼요.
        if (inputPanels.isEmpty()) {
            throw new IllegalStateException(notGoodInputPanels(1));
        }

        // 2 : 계산식이 올바르지 않습니다. 처음에 + , * , / , ) , . 이 오면 안돼요.
        if (inputPanels.charAt(0) == '+' || inputPanels.charAt(0) == '*' || inputPanels.charAt(0) == '/'
           || inputPanels.charAt(0) == ')' || inputPanels.charAt(0) == '.') {
            throw new IllegalStateException(notGoodInputPanels(2));
        }

        // 6 : 계산식이 올바르지 않습니다. 마지막에 + , - , * , / , ( , . 이 오면 안돼요.
        if (inputPanels.charAt(inputPanels.length()-1) == '+' || inputPanels.charAt(inputPanels.length()-1) == '-'
           || inputPanels.charAt(inputPanels.length()-1) == '*' || inputPanels.charAt(inputPanels.length()-1) == '/'
           || inputPanels.charAt(inputPanels.length()-1) == '(' || inputPanels.charAt(inputPanels.length()-1) == '.') {
            throw new IllegalStateException(notGoodInputPanels(6));
        }

        // 3, 4
        for (int i = 0; i < inputPanels.length(); i++) {
            char inputPanel2 = inputPanels.charAt(i);

            // 3 : 계산식이 올바르지 않습니다. 부호가 중첩되었거나 괄호와 부호의 조합이 옳지않습니다.
            if (i > 0) {
                char inputPanel1 = inputPanels.charAt(i - 1);
                if (!((48 <= inputPanel1) && (inputPanel1 <= 57))     // 앞이 숫자가 아니고     ++ , +* , +/ , +)
                        && !((48 <= inputPanel2) && (inputPanel2 <= 57))  // 뒤도 숫자가 아니고     -+ , -* , -/ , -)
                        && inputPanel2 != '-'                             // 뒤가 - 가 아니고      *+ , ** , */ , *)
                        && inputPanel1 != ')'                             // 앞이 ) 가 아니고      /+ , /* , // , /)
                        && inputPanel2 != '('){                           // 뒤가 ( 가 아니면      (+ , (* , (/ , ()
                    throw new IllegalStateException(notGoodInputPanels(3));
                }
            }

            // 4 : 계산식이 올바르지 않습니다. 소수점이 잘못되었습니다.
            if (inputPanel2 == '.' && i > 1 ) {                   // '.' 의 양옆에는 숫자 여야만 한다.
                char inputPanel1 = inputPanels.charAt(i - 1);
                char inputPanel3 = inputPanels.charAt(i + 1);
                if (!((48 <= inputPanel1) && (inputPanel1 <= 57) && (48 <= inputPanel3) && (inputPanel3 <= 57))) {
                    throw new IllegalStateException(notGoodInputPanels(4));
                }
            }

            if (inputPanel2 == '(') { count1++; }
            if (inputPanel2 == ')') { count2++; }
        }

        // 5 : 계산식이 올바르지 않습니다. 괄호의 쌍이 맞지 않습니다.
        if (count1 != count2){                                    // '(' 와 ')' 의 갯수가 맞지 않으면
            throw new IllegalStateException(notGoodInputPanels(5));
        }

        bracketCount = count1;

        return bracketCount;
    }    // 계산식이 올바른지 체크

    public String makeNewPanels(String inputPanels) {
        String newPanels = "";
        for (int i = 0; i < inputPanels.length(); i++) {
            char inputPanel = inputPanels.charAt(i);

            if (inputPanel == '(' && i != 0 && (((48 <= inputPanels.charAt(i - 1))          // ( 가 있다면
                                                  && (inputPanels.charAt(i - 1) <= 57))     // ( 이전 (i - 1)  이 숫자 이거나 ) 일때
                                                  || inputPanels.charAt(i - 1) == ')')) {   // 이 사이에 * 를 넣는다
                newPanels += '*';                                                           // '(' 의 전에 '*' 을 넣어야하므로
            }                                                                               // '(' 를 newPanels 에 넣기전에 '*' 를 넣음
            if (inputPanel == ')' && i != (inputPanels.length() - 1)        // ) 가 있다면
                                  && (48 <= inputPanels.charAt(i + 1))      // ) 다음 (i + 1) 이 숫자 라면 ('(' 인 경우는 위에서 했음)
                                  && (inputPanels.charAt(i + 1) <= 57)) {   // 이 사이에 * 을 넣는다.
                newPanels += inputPanel;                                    // ')' 의 후에 '*' 을 넣어야하므로
                newPanels += '*';                                           // ')' 를 newPanels 에 넣은후에 '*' 를 넣음
                continue;                                                   // 그리고 continue; 로
            }
            newPanels += inputPanel;
        }
        return newPanels;
    }       // 정확한 계산식으로 바꿈

    public String removeBracketOne(String newPanels) {
        int indexFront = 0;
        int indexBack = 0;
        double res;
        String liveCalculate;
        String newNewPanels = "";

        for (int i = 0; i < newPanels.length(); i++) {
            char ch = newPanels.charAt(i);
            if (ch == ')') { indexBack = i; break; }
            if (ch == '(') { indexFront = i; }
        }
        liveCalculate = newPanels.substring(indexFront + 1, indexBack); // = 첫번재 괄호 안 내용
        for (int i = 0; i < indexFront; i++){
            char newPanel = newPanels.charAt(i);
            newNewPanels += newPanel;                                   // 첫번째째 괄호 오기전까지 누적
        }
        newNewPanels += makeResultOne(liveCalculate);                   // 누적 + 첫번째 괄호 안 내용의 값
        for (int i = indexBack + 1; i < newPanels.length(); i++){
            char newPanel = newPanels.charAt(i);
            newNewPanels += newPanel;                                   // 누적 + 값 + 누적
        }
        newPanels = newNewPanels;                                       // 반환할건데 알아보기 쉽게 다시 바꿈
        return newPanels;
    }      // 괄호가 있을경우만 실행하기 약속~

    public String makeResultOne(String liveCalculate) {

        // * , / 먼저 계산
        while (liveCalculate.contains("*") || liveCalculate.contains("/")) {
            int Oper = 0;
            double num1;
            double num2;
            List<Integer> indexOpers = new ArrayList<>();
            double res;

            // liveCalculate 부호의 인덱스 값을 기록하기 위함
            for (int i = 0; i < liveCalculate.length(); i++) {
                char ch = liveCalculate.charAt(i);
                if (ch == '+' || ch == '*' || ch == '/') {
                    indexOpers.add(i);
                } else if (ch == '-') {                             // 음수 부호 뺄셈 부호 구분
                    if (i == 0) { continue; }
                    char frontCh = liveCalculate.charAt(i - 1);
                    if (frontCh == '+' || frontCh == '-' || frontCh == '*' || frontCh == '/' || frontCh == '(') { continue; }
                    indexOpers.add(i);
                }
            }

            // * , / 중 하나라도 있으면 그중 가장 왼쪽인덱스   하나도 없으면  while 이 실행조차 안됨
            for (int i = 0; i < liveCalculate.length(); i++) {
                char ch = liveCalculate.charAt(i);

                if (ch == '*' || ch == '/') { Oper = i; break; }
            }

            // 시작  1  -  2  +  4  -  2  끝
            int i1, i2, i3, i4, i5;         // i1 -> i2 -> i3 -> i4 -> i5
            i1 = 0;
            if(indexOpers.indexOf(Oper) > 0) {
                i2 = indexOpers.get(indexOpers.indexOf(Oper) - 1);
            } else {
                i2 = 0;
            }
            i3 = Oper;
            if((indexOpers.indexOf(Oper) + 1) < indexOpers.size()){
                i4 = indexOpers.get(indexOpers.indexOf(Oper) + 1);
            } else {
                i4 = 0;
            }
            i5 = liveCalculate.length();

            if (indexOpers.indexOf(Oper) == 0 && indexOpers.indexOf(Oper) + 1 == indexOpers.size()) {  // * , / 가 맨앞이고 뒤에 아무것도 없을경우
                num1 = Double.parseDouble(liveCalculate.substring(i1, i3));
                num2 = Double.parseDouble(liveCalculate.substring(i3 + 1, i5));                                              // num 은 부호 미포함
                res = baseCalculate(liveCalculate.charAt(Oper), num1, num2);
                liveCalculate = String.valueOf(res);                                                                         // liveCalculate 는 부호 포함
            } else if (indexOpers.indexOf(Oper) == 0 && indexOpers.indexOf(Oper) + 1 < indexOpers.size()) { // * , / 가 맨앞이고 뒤에 무언가 있을경우
                num1 = Double.parseDouble(liveCalculate.substring(i1, i3));
                num2 = Double.parseDouble(liveCalculate.substring(i3 + 1, i4));                                              // num 은 부호 미포함
                res = baseCalculate(liveCalculate.charAt(Oper), num1, num2);
                liveCalculate = String.valueOf(res) + liveCalculate.substring(i4, i5);                                       // liveCalculate 는 부호 포함
            }
            if (indexOpers.indexOf(Oper) != 0 && (indexOpers.indexOf(Oper) + 1) == indexOpers.size()) { // * , / 가 맨앞이 아니고 뒤에 아무것도 없을경우
                num1 = Double.parseDouble(liveCalculate.substring(i2 + 1, i3));
                num2 = Double.parseDouble(liveCalculate.substring(i3 + 1, i5));                                              // num 은 부호 미포함
                res = baseCalculate(liveCalculate.charAt(Oper), num1, num2);
                liveCalculate = liveCalculate.substring(i1, i2 + 1) + String.valueOf(res);                                   // liveCalculate 는 부호 포함
            } else if (indexOpers.indexOf(Oper) != 0 && (indexOpers.indexOf(Oper) + 1) < indexOpers.size()) { // * , / 가 맨앞이 아니고 뒤에 무언가 있을경우
                num1 = Double.parseDouble(liveCalculate.substring(i2 + 1, i3));
                num2 = Double.parseDouble(liveCalculate.substring(i3 + 1, i4));                                              // num 은 부호 미포함
                res = baseCalculate(liveCalculate.charAt(Oper), num1, num2);
                liveCalculate = liveCalculate.substring(i1, i2 + 1) + String.valueOf(res) + liveCalculate.substring(i4, i5); // liveCalculate 는 부호 포함
            }
        }
        // + , -  계산
        while (liveCalculate.contains("+") || liveCalculate.contains("-")) {
            int Oper = 0;
            double num1;
            double num2;
            List<Integer> indexOpers = new ArrayList<>();
            double res;

            // liveCalculate 부호의 인덱스 값을 기록하기 위함
            for (int i = 0; i < liveCalculate.length(); i++) {
                char ch = liveCalculate.charAt(i);
                if (ch == '+' || ch == '*' || ch == '/') {
                    indexOpers.add(i);
                } else if (ch == '-') {                             // 음수 부호 뺄셈 부호 구분
                    if (i == 0) { continue; }
                    char frontCh = liveCalculate.charAt(i - 1);
                    if (frontCh == '+' || frontCh == '-' || frontCh == '*' || frontCh == '/' || frontCh == '(') { continue; }
                    indexOpers.add(i);
                }
            }
            if(indexOpers.isEmpty()) { break; } // 식에서 - 가 존재해서 while 이 실행 됐지만 음수부호 라고 판단될경우 계산종료

           // + , - 중 하나라도 있으면 indexOpers 에 포함되었을테니까 그중 가장 왼쪽인덱스 하나도 없으면 이미 위에서 계산종료됨
            for (int i = 0; i < liveCalculate.length(); i++) {
                char ch = liveCalculate.charAt(i);
                if (ch == '+') {
                    Oper = i; break;
                } else if (ch == '-') {                             // 음수 부호 뺄셈 부호 구분
                    if (i == 0) { continue; }
                    char frontCh = liveCalculate.charAt(i - 1);
                    if (frontCh == '+' || frontCh == '-' || frontCh == '*' || frontCh == '/' || frontCh == '(') { continue; }
                    Oper = i; break;
                }
            }

            // 시작  1  -  2  +  4  -  2  끝
            int i1, i2, i3, i4, i5;         // i1 -> i2 -> i3 -> i4 -> i5
            i1 = 0;
            if(indexOpers.indexOf(Oper) > 0) {
                i2 = indexOpers.get(indexOpers.indexOf(Oper) - 1);
            } else {
                i2 = 0;
            }
            i3 = Oper;
            if((indexOpers.indexOf(Oper) + 1) < indexOpers.size()){
                i4 = indexOpers.get(indexOpers.indexOf(Oper) + 1);
            } else {
                i4 = 0;
            }
            i5 = liveCalculate.length();

            if (indexOpers.indexOf(Oper) == 0 && indexOpers.indexOf(Oper) + 1 == indexOpers.size()) {  // + , - 가 맨앞이고 뒤에 아무것도 없을경우
                num1 = Double.parseDouble(liveCalculate.substring(i1, i3));
                num2 = Double.parseDouble(liveCalculate.substring(i3 + 1, i5));                                             // num 은 부호 미포함
                res = baseCalculate(liveCalculate.charAt(Oper), num1, num2);
                liveCalculate = String.valueOf(res);                                                                        // liveCalculate 는 부호 포함
            } else if (indexOpers.indexOf(Oper) == 0 && indexOpers.indexOf(Oper) + 1 < indexOpers.size()) { // + , - 가 맨앞이고 뒤에 무언가 있을경우
                num1 = Double.parseDouble(liveCalculate.substring(i1, i3));
                num2 = Double.parseDouble(liveCalculate.substring(i3 + 1, i4));                                             // num 은 부호 미포함
                res = baseCalculate(liveCalculate.charAt(Oper), num1, num2);
                liveCalculate = String.valueOf(res) + liveCalculate.substring(i4, i5);                                      // liveCalculate 는 부호 포함
            }
            if (indexOpers.indexOf(Oper) != 0 && (indexOpers.indexOf(Oper) + 1) == indexOpers.size()) { // + , - 가 맨앞이 아니고 뒤에 아무것도 없을경우
                num1 = Double.parseDouble(liveCalculate.substring(i2 + 1, i3));
                num2 = Double.parseDouble(liveCalculate.substring(i3 + 1, i5));                                             // num 은 부호 미포함
                res = baseCalculate(liveCalculate.charAt(Oper), num1, num2);
                liveCalculate = liveCalculate.substring(i1, i2 + 1) + String.valueOf(res);                                  // liveCalculate 는 부호 포함
            } else if (indexOpers.indexOf(Oper) != 0 && (indexOpers.indexOf(Oper) + 1) < indexOpers.size()) { // + , - 가 맨앞이 아니고 뒤에 무언가 있을경우
                num1 = Double.parseDouble(liveCalculate.substring(i2 + 1, i3));
                num2 = Double.parseDouble(liveCalculate.substring(i3 + 1, i4));                                             // num 은 부호 미포함
                res = baseCalculate(liveCalculate.charAt(Oper), num1, num2);
                liveCalculate = liveCalculate.substring(i1, i2 + 1) + String.valueOf(res) + liveCalculate.substring(i4, i5); // liveCalculate 는 부호 포함
            }
        }
        return liveCalculate;
    }     // 가장 안쪽의 괄호 안에 있는 값 계산

    public String makeResult (String inputPanels) {
        String res;
        int bracketCount = checkInputPanels(inputPanels);       // inputPanels 받은거를 체크하고 () 몇쌍인지 확인
        String newPanels = makeNewPanels(inputPanels);          // 정확한 계산식인 newPanels 를 만듬

        if (bracketCount > 0) {
            for (int i = 0; i < bracketCount; i++) {
                newPanels = removeBracketOne(newPanels);
            }
        }

        res = makeResultOne(newPanels);

        return res;
    }         // 결과 값

    public String updateResult (String inputPanels, Integer underPoint) {
        String res = makeResult(inputPanels);
        String updateResult = "";
        int point = 0;
        int pointCounter = 0;

        // 결과가 너무커서 E 로 표기되면 그냥 ㄱㄱ
        for(int i = 0; i < res.length(); i++) {
            if(res.charAt(i) == 'E') {
                return res;
            }
        }

        // 소수점 없으면 그냥
        if(!res.contains(".")) {
            if (underPoint == 0 || underPoint == -1 ) {
                return res;
            }
            String newRes = "";
            newRes = res;
            newRes += ".";
            for(int i = 0; i < underPoint; i++) {
                newRes += "0";
            }
            return newRes;
        }

        // res 의 '.' 의 위치와 '.' 이후의 갯수 구하기 (소수점 몇째자리 구하기)
        for (int i = 0; i < res.length(); i++) {
            if (res.charAt(i)=='.') {
                point = i;
            }
        }

        pointCounter = res.length() - 1 - point;

        // default 값일때
        if (underPoint == -1) {

            // 소수점 첫째자리이고 그 값이 0 이라면 정수출력
            if(pointCounter == 1  && res.charAt(res.length() - 1) == '0') { for (int i = 0; i < point; i++) { updateResult += res.charAt(i); } }

            // 아니라면 그냥 출력
            else { updateResult = res; }

            return updateResult;
        }

        // underPoint 가 결과 값자리수 보다 클때
        if(pointCounter < underPoint) {
            updateResult = res;
            for(int i = 0; i < underPoint - pointCounter; i++) { updateResult += "0"; }
            return updateResult;
        }

        // default 가 아닐때
        double value = Double.parseDouble(res);
        double scale = Math.pow(10, underPoint);
        double rounded = Math.round(value * scale) / scale;

        String stringRounded= String.valueOf(rounded);

        // underPoint 가 0 일때 정수형태로 출력
        if (underPoint == 0) {
            String newStringRounded = "";
            for(int i = 0; i < stringRounded.length() - 2; i++) {
                newStringRounded += stringRounded.charAt(i);
            }
            return newStringRounded;
        }

        return stringRounded;
    }   // 소수점 자리에맞춰 결과값 변경
}
//       index :   0 1 2 3 4 5 6 7 8 9                     liveCalculate.get(3) == "*"
// liveCalculate { 4 + 5 * 2 * 4 + 1 0 } 4+5*2*4+10        liveCalculate.indexOf("*" or "/") == Oper == 3

//                                                        liveCalculate.get(indexOpers.get(Oper) == 가장 왼쪽에있는 * or / 값
// liveCalculate  +     *     *     +                      liveCalculate.get(indexOpers.get(1)) == "+"
// indexOpers { "1" , "3" , "5" . "7"  }  + * * +         indexOpers.get(1) == 3
//       index : 0     1     2     3                      indexOpers.indexOf(Oper) == 1 == liveCalculate 의해당부호의 인덱스
//                                         indexOpers.get(indexOpers.indexOf(Oper) - 1) == liveCalculate 의 앞 부호의 인덱스
//                                         indexOpers.get(indexOpers.indexOf(Oper) + 1) == liveCalculate 의 뒷 부호의 인덱스

// 즉, num1 = liveCalculate.substring( indexOpers.get(indexOpers.indexOf(Oper) - 1) , Oper )
//    num2 = liveCalculate.substring ( Oper + 1 , indexOpers.get(indexOpers.indexOf(Oper) + 1))

// 여기서 indexOpers.indexOf(Oper) - 1 가 없을경우 : 해당부호가 제일 왼쪽일 경우
//       indexOpers.indexOf(Oper) + 1 가 없을경우 : 해당부호가 제일 오른쪽일 경우 생각해야함

// 괄호 쌍 수는 1
// inputPanels  = 4+(4 + 5 * 2 * 4 + 1 0)6 => .makeNewPanels =>
// newPanels    = 4+(4 + 5 * 2 * 4 + 1 0)*6 => .makeResultOne =>                         // liveCalculate = 4 + 5 * 2 * 4 + 1 0 = 54
// newNewPanels = 4+(54)*6 =>  .removeBracketOne =>
//              4+54*6 => .makeResultOne =>
//              328
