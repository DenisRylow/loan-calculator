package goodbank;

import goodbank.dto.MonthlyLoanStatus;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Iterator;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(properties = {
        "goodbank.default-interest-rate=2000",
        "goodbank.default-loan-sum=200000",
})
@Ignore
public class AbstractTest {
    protected static final int INTEREST_RATE = 2000;// 10%

    protected static void assertScheduleIsCorrect(List<MonthlyLoanStatus> schedule) {
        Assert.assertTrue(schedule.size() > 1);
        Iterator<MonthlyLoanStatus> iter = schedule.iterator();
        MonthlyLoanStatus previous = iter.next();
        assertPayment(previous);
        while (iter.hasNext()) {
            MonthlyLoanStatus current = iter.next();
            assertPayment(current);
            Assert.assertEquals((
                    previous.getYear() - 1)* 12 + previous.getMonth() + 1,
                    (current.getYear() - 1)* 12 + current.getMonth()
            );
            Assert.assertEquals(
                    current.getDebt().add(current.getPaymentToPrincipal()),
                    previous.getDebt()
            );
            previous = current;
        }
    }

    private static void assertPayment(MonthlyLoanStatus status) {
        Assert.assertEquals(status.getTotalPayment(), status.getPaymentToInterest().add(status.getPaymentToPrincipal()));
    }
}
