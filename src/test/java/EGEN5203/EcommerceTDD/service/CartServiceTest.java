package EGEN5203.EcommerceTDD.service;

import EGEN5203.EcommerceTDD.dto.AddToCartDto;
import EGEN5203.EcommerceTDD.dto.ProductDto;
import EGEN5203.EcommerceTDD.dto.UpdateCartDto;
import EGEN5203.EcommerceTDD.model.*;
import EGEN5203.EcommerceTDD.repo.CartRepo;
import EGEN5203.EcommerceTDD.repo.CartItemRepo;
import EGEN5203.EcommerceTDD.repo.ProductRepo;
import EGEN5203.EcommerceTDD.repo.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @InjectMocks
    private CartService cartService;

    @Mock
    private CartRepo cartRepo;

    @Mock
    private CartItemRepo cartItemRepo;

    @Mock
    private ProductRepo productRepo;

    @Mock
    private UserRepo userRepo;

    private Users customer;
    private Product product;
    private Cart cart;
    private CartItem cartItem;
    private AddToCartDto addToCartDto;
    private UpdateCartDto updateCartDto;

    @BeforeEach
    void setUp() {
        customer = new Users();
        customer.setUser_id(1L);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("john.doe@example.com");

        product = new Product();
        product.setProductId(1L);
        product.setName("Apple");
        product.setDescription("Red Color");
        product.setPrice(499.99);
        product.setStock(10);
        product.setCategory("Fruit");

        cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setProduct(product);
        cartItem.setQuantity(2);

        cart = new Cart();
        cart.setId(1L);
        cart.setCustomer(customer);
        cart.setItems(Collections.singletonList(cartItem));

        ProductDto productDto = new ProductDto();
        productDto.setProductId(1L);
        productDto.setQuantity(2);

        addToCartDto = new AddToCartDto();
        addToCartDto.setCustomerId(1L);
        addToCartDto.setProducts(Collections.singletonList(productDto));

        ProductDto updateProductDto = new ProductDto();
        updateProductDto.setProductId(1L);
        updateProductDto.setQuantity(3);

        updateCartDto = new UpdateCartDto();
        updateCartDto.setCustomerId(1L);
        updateCartDto.setProducts(Collections.singletonList(updateProductDto));
    }

    @Test
    void createCart_Success() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepo.findById(1L)).thenReturn(Optional.of(product));
        when(cartRepo.save(any(Cart.class))).thenReturn(cart);

        Cart result = cartService.createCart(addToCartDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1, result.getItems().size());
        assertEquals(2, result.getItems().get(0).getQuantity());

        verify(cartRepo, times(1)).save(any(Cart.class));
    }

    @Test
    void createCart_CustomerNotFound() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.createCart(addToCartDto);
        });

        assertEquals("Customer not found", exception.getMessage());
    }

    @Test
    void createCart_ProductNotFound() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepo.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.createCart(addToCartDto);
        });

        assertEquals("Product not found", exception.getMessage());
    }

    @Test
    void findAll_Success() {
        when(cartRepo.findAll()).thenReturn(Collections.singletonList(cart));

        List<Cart> result = cartService.findAll();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void findOne_Success() {
        when(cartRepo.findById(1L)).thenReturn(Optional.of(cart));

        Cart result = cartService.findOne(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void findOne_NotFound() {
        when(cartRepo.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.findOne(1L);
        });

        assertEquals("Cart not found", exception.getMessage());
    }

    @Test
    void updateCart_Success() {
        when(cartRepo.findById(1L)).thenReturn(Optional.of(cart));
        when(userRepo.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepo.findById(1L)).thenReturn(Optional.of(product));
        when(cartRepo.save(any(Cart.class))).thenReturn(cart);

        Cart result = cartService.updateCart(1L, updateCartDto);

        assertNotNull(result);
        verify(cartItemRepo, times(1)).deleteByCartId(1L);
    }

    @Test
    void updateCart_CartNotFound() {
        when(cartRepo.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.updateCart(1L, updateCartDto);
        });

        assertEquals("Cart not found", exception.getMessage());
    }

    @Test
    void updateCart_ProductNotFound() {
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setItems(new ArrayList<>()); // Mutable list

        lenient().when(cartRepo.findById(1L)).thenReturn(Optional.of(cart));
        updateCartDto.setCustomerId(2L);
        lenient().when(userRepo.findById(2L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.updateCart(1L, updateCartDto);
        });

        assertEquals("Product not found", exception.getMessage());
    }

    @Test
    void deleteCart_Success() {
        lenient().when(cartRepo.existsById(1L)).thenReturn(true);

        cartService.deleteCart(1L);

        verify(cartRepo, times(1)).deleteById(1L);
    }
}