public class HourlyEmployee extends Employee {
    private int tenthsWorked;
    HourlyPayGrade grade;

    public HourlyEmployee() {
    }

    public void calculatePay() {
        System.out.print(grade.rate());
    }

}
