public enum HourlyPayGrade {
    JOURNEYMAN{
        public double rate() {
            return 1.5;
        }
    },
    MASTER {
        public double rate() {
            return 2.0;
        }
    };

    public abstract double rate();

}
