package com.sda.onlineAuction.service;


import com.sda.onlineAuction.dto.ProductDto;
import com.sda.onlineAuction.mapper.ProductMapper;
import com.sda.onlineAuction.model.Product;
import com.sda.onlineAuction.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    public void add(ProductDto productDto, MultipartFile multipartFile) {
        Product product = productMapper.map(productDto, multipartFile);
        productRepository.save(product);
    }

    public List<ProductDto> getAllProductDtos() {
        List<Product> productList = productRepository.findAll();
        List<ProductDto> result = new ArrayList<>();
        for (Product product : productList) {
            ProductDto productDto = productMapper.map(product);
            result.add(productDto);
        }
        return result;
    }

    public List<ProductDto> getAllProductDtosWithStream() {
        List<Product> productList = productRepository.findAll();
      return productList.stream()
              .map(productMapper::map)
              .collect(Collectors.toList());
    }

    public Optional<ProductDto> getProductDtoById(String productId) {
        Optional<Product> optionalProduct = productRepository.findById(Integer.valueOf(productId));
        if (!optionalProduct.isPresent()) {
            return Optional.empty();
        }
        Product product = optionalProduct.get();
        ProductDto productDto = productMapper.map(product);

        return Optional.of(productDto);
    }
}
