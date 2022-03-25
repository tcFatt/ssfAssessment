package ssf.POAPP;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ssf.POAPP.service.QuotationService;

@SpringBootTest
class POAPPApplicationTests {

	@Autowired
	private QuotationService quotationSvcTest;

	@Test
	void contextLoads() {

		List<String> items = new ArrayList<>();
			items.add("durian");
			items.add("plum");
			items.add("pear");

		Optional<Quotation> optQuotation = quotationSvcTest.getQuotations(items);

		Assertions.assertTrue(optQuotation.isEmpty());
	}

}
