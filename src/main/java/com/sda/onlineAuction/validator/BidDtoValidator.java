package com.sda.onlineAuction.validator;

import com.sda.onlineAuction.dto.BidDto;
import com.sda.onlineAuction.model.Bid;
import com.sda.onlineAuction.model.Product;
import com.sda.onlineAuction.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Optional;

@Service
public class BidDtoValidator {

    @Autowired
    private ProductRepository productRepository;

    public void validate(BidDto bidDto, BindingResult bindingResult, String productId) {

        String bidValue = bidDto.getValue();
        // 1 - sa nu fie gol
        if (bidValue.isEmpty()) {
            FieldError fieldError = new FieldError("bidDto", "value", "Value should not be empty");
            bindingResult.addError(fieldError);
            return;
        }

        // 2 - sa fie numar pozitiv
        Integer bidValueAsNumber = 0;
        try {
            bidValueAsNumber = Integer.valueOf(bidValue);
        } catch (NumberFormatException numberFormatException) {
            FieldError fieldError = new FieldError("bidDto", "value", "Value should be a number");
            bindingResult.addError(fieldError);
            return;
        }
        if (bidValueAsNumber <= 0) {
            FieldError fieldError = new FieldError("bidDto", "value", "Value should be a positive number");
            bindingResult.addError(fieldError);
            return;
        }

        // 3 - sa fie mai mare sau egal ca starting price
        Optional<Product> optionalProduct = productRepository.findById(Integer.valueOf(productId));
        if (!optionalProduct.isPresent()) {
            FieldError fieldError = new FieldError("bidDto", "value", "Product id is not valid");
            bindingResult.addError(fieldError);
            return;
        }
        Product product = optionalProduct.get();
        if (product.getStartingPrice() > bidValueAsNumber) {
            FieldError fieldError = new FieldError("bidDto", "value", "Value must be at least starting price");
            bindingResult.addError(fieldError);
            return;
        }

        // 4 - sa fie mai mare ca cel mai mare bid existent (daca sunt bid-uri)
        List<Bid> bidList = product.getBidsList();
        if (!bidList.isEmpty()) {
            int max = 0;
            for (Bid bid : bidList) {
                if (max < bid.getValue()) {
                    max = bid.getValue();
                }
            }
            if (bidValueAsNumber <= max) {
                FieldError fieldError = new FieldError("bidDto", "value", "Value must be greater than lasted bid");
                bindingResult.addError(fieldError);
                return;
            }
        }
    }
}
