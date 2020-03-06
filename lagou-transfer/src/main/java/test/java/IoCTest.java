import com.lagou.edu.dao.AccountDao;
import com.lagou.edu.factory.BeanFactory;
import com.lagou.edu.factory.ProxyFactory;
import com.lagou.edu.service.TransferService;
import org.junit.Test;

/*import com.lagou.edu.dao.impl.ADaoImp1;*/

/**
 * @author 应癫
 */
public class IoCTest {


    @Test
    public void testIoC() throws Exception {
        ProxyFactory proxyFactory = (ProxyFactory) BeanFactory.getBean("proxyFactory");
        TransferService transferService = (TransferService) proxyFactory.getProxy(BeanFactory.getBean("TransferServiceImpl")) ;
        transferService.transfer("6029621011000","6029621011001",100);
    }


}