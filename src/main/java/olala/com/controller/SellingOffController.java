package olala.com.controller;

import lombok.extern.slf4j.Slf4j;
import olala.com.auth.CustomUserDetail;
import olala.com.entities.Orders;
import olala.com.entities.Users;
import olala.com.repository.OrdersRepository;
import olala.com.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.UUID;


@Controller
@RequestMapping("/admin/selloff")
public class SellingOffController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @GetMapping("/")
    public String viewSellOff(
            @AuthenticationPrincipal UserDetails userDetails, Model model

    ) {

        // Lấy tên người dùng từ thông tin người dùng đã xác thực
        String performerName = userDetails.getUsername();

        // Gán giá trị vào model
        model.addAttribute("performerName", performerName);

        return "SellOffPage";
    }

    @PostMapping("/add")
    public String createBill(
            @ModelAttribute("orders") Orders orders,
            Model model
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
        Long userId = customUserDetail.getUsers().getId();

        Users users = usersRepository.getById(userId);

        String ma = UUID.randomUUID().toString();


        orders.setEmployee(users);
        orders.setUsers(users);
        orders.setOrderInfo(ma);

        orders.setStatus(1);

        this.ordersRepository.save(orders);

        // Chuyển hướng đến trang "/admin/selloff/" sau khi tạo hóa đơn thành công
        return "redirect:/admin/selloff/";
    }

}
