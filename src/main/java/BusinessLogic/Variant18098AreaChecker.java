package BusinessLogic;

import interfaces.Checkable;

public class Variant18098AreaChecker implements Checkable {
    @Override
    public boolean check(double x, double y, double r) {
        if (x > 0 && y < 0) return false;
        if (x <= 0 && x >= -r && y <= 0 && y>= -r) return true;
        if (x >= 0 && y >= 0 && x*x+y*y <= r*r) return true;
        if (x <= 0 && y >= 0 && y <= 2*x + r) return true;
        return false;
    }
}
