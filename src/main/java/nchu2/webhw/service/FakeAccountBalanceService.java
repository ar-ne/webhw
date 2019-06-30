package nchu2.webhw.service;

import org.springframework.stereotype.Service;

/**
 * 生成一个假的账户余额
 */
@Service
public class FakeAccountBalanceService {
    private double bal = genBal();

    private static double genBal() {
        return Math.random() > 0.5 ? Math.random() * 1000 / Math.random() : Math.random() * 764 + 10000;
    }

    public double pay(double money) {
        if (money > bal) return -1;
        else return bal -= money;
    }

    public double getBal() {
        return bal;
    }
}
