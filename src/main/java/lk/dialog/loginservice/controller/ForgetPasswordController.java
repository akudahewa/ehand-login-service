package lk.dialog.loginservice.controller;

import lk.dialog.loginservice.exception.ResourceNotFoundException;
import lk.dialog.loginservice.model.ForgotPasswordRequest;
import lk.dialog.loginservice.model.NewPasswordRequest;
import lk.dialog.loginservice.model.User;
import lk.dialog.loginservice.payload.ApiResponse;
import lk.dialog.loginservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/api/auth")
public class ForgetPasswordController {

    UserRepository userRepository;

    private final JavaMailSender mailSender;
    BCryptPasswordEncoder bCryptPasswordEncoder;

    ForgetPasswordController(UserRepository userRepository, JavaMailSender mailSender,BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepository = userRepository;
        this.mailSender = mailSender;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> processForgotPasswordToken(@RequestBody ForgotPasswordRequest request){
        log.info("POST -> forgot password {}",request.getSender());
        String token = RandomString.make(30);
        // update reset password token
        User user = userRepository.findByEmail("akudahewa@gmail.com")
                    .orElseThrow(() -> new ResourceNotFoundException("User", "email", "akudahewa@gmail.com"));;
        user.setResetPasswordToken(token);
        userRepository.save(user);
        SimpleMailMessage passwordResetMail = new SimpleMailMessage();
        passwordResetMail.setFrom("akudahewa@gmail.com");
        passwordResetMail.setTo(request.getSender());
        passwordResetMail.setSubject("Reset password");
        passwordResetMail.setText("Please reset your password");
        mailSender.send(passwordResetMail);


        return ResponseEntity.ok().body(new ApiResponse(true, "User registered successfully"));
    }

    @GetMapping("/password/reset")
    public User loadDisplayNewPasswordForm(@RequestParam(value = "token") String token){
        log.info("GET -> load display reset password form, token {}",token);
        Optional<User> user =userRepository.findByResetPasswordToken(token);
        if(!user.isPresent()){
            throw  new ResourceNotFoundException("User","token","Not a valid token :"+token);
        }
        return user.get();

    }

    @PostMapping("/password/reset")
    public User setNewPassword(@RequestBody NewPasswordRequest newPasswordRequest){
        Optional<User> user =userRepository.findByResetPasswordToken(newPasswordRequest.getToken());
        if(!user.isPresent()){
            throw  new ResourceNotFoundException("User","token","Not a valid token :"+newPasswordRequest.getToken());
        }
        if(newPasswordRequest.getNewPassword().equals(newPasswordRequest.getConfirmPassword())){
            bCryptPasswordEncoder.encode(newPasswordRequest.getNewPassword());
        }
        user.get().setPassword(bCryptPasswordEncoder.encode(newPasswordRequest.getNewPassword()));
        user.get().setResetPasswordToken(null);
        return user.get();

    }
}
