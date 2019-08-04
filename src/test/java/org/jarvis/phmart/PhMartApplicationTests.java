package org.jarvis.phmart;

import org.jarvis.core.util.ContextUtil;
import org.jarvis.orm.hibernate.domain.reference.Status;
import org.jarvis.orm.hibernate.repository.EntityRepository;
import org.jarvis.phmart.model.entity.Category;
import org.jarvis.phmart.model.entity.ExchangeRate;
import org.jarvis.phmart.model.entity.Supplier;
import org.jarvis.phmart.model.entity.reference.Form;
import org.jarvis.sercurity.domain.entity.JarvisProfile;
import org.jarvis.sercurity.domain.entity.JarvisUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PhMartApplicationTests {

    @Autowired
    private EntityRepository repository;

    //@Test
    public void contextLoads() {

    }

    //@Test
    public void createExchangeRate() {
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setDesc("1$=" + 4100 + ContextUtil.getProperty("kh_currency"));
        exchangeRate.setValue(4100);
        exchangeRate.setFromCurrency("USD");
        exchangeRate.setToCurrency("KHR");
        repository.saveOrUpdate(exchangeRate);
    }

    // @Test
    public void createForm() {
        Form form1 = new Form("Package", "Package");
        Form form2 = new Form("Bottle", "Bottle");
        Form form3 = new Form("Box", "Box");
        repository.saveOrUpdate(form1);
        repository.saveOrUpdate(form2);
        repository.saveOrUpdate(form3);
    }

    //@Test
    public void crreateCategory() {
        Category category1 = new Category("Shampoo", "Shampoo");
        Category category2 = new Category("Ice-cream", "Ice-cream");
        Category category3 = new Category("Drink", "Drink");
        repository.saveOrUpdate(category1);
        repository.saveOrUpdate(category2);
        repository.saveOrUpdate(category3);

    }

    //@Test
    public void createUser() {
        JarvisUser user = new JarvisUser();
        user.setStatus(Status.ACTIVE);
        user.setUsername("admin");
        user.setPassword("$2a$10$C1bY6NUaiej9MRM48/xV.uHk1EfZUatdth.wYg25r7PjEzG5JucA6");
        user.addProfile(new JarvisProfile("ADMIN", "Role normal user").setStatus(Status.ACTIVE));
        repository.saveOrUpdate(user);
    }

    //@Test
    public void test() {
        Category category = new Category();
        category.setNameEn("Good things");
        category.setNameKh("សសដ");
        category.setLabel("Good things");
        category.setDescription("Testing Shampoo");
        category.setStatus(Status.ACTIVE);
        category.setCreatedDate(new Date());
        category.setLastModifiedDate(new Date());
        category.setLastModifiedBy("admin");
        category.setCreatedBy("admin");
        repository.saveOrUpdate(category);
    }

    @Test
    public void testCreateSupplier() {
        Supplier supplier = new Supplier();
        supplier.setCompanyName("UST");
        repository.saveOrUpdate(supplier);
    }
}

