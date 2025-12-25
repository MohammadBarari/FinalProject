package org.example.service.user;

import jakarta.transaction.Transactional;
import org.example.domain.Orders;
import org.example.domain.PassAndUser;
import org.example.domain.Users;
import org.example.dto.password.ChangingPasswordDto;
import org.example.dto.admin.FindFilteredOrdersDto;
import org.example.exeptions.NotFoundException.NotFoundUser;
import org.example.exeptions.password.AllNotBeLetterOrDigits;
import org.example.exeptions.password.PassNot8Digits;
import org.example.exeptions.password.PasswordNotCorrect;
import org.example.exeptions.password.UnableToChangePassWord;
import org.example.repository.passAndUser.PassAndUserRepository;
import org.example.repository.user.BaseUserRepository;
import org.example.service.credit.CreditService;
import org.example.service.emailToken.EmailTokenService;
import org.example.service.mapStruct.EntityMapper;
import org.example.service.offer.OfferService;
import org.example.service.order.OrderService;
import org.example.service.subHandler.SubHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import java.util.Optional;


public abstract class BaseUserServiceImp <T extends Users> implements BaseUserService<T> {

    private final BaseUserRepository<T> baseUserRepository ;
    protected final OrderService orderService ;
    protected final OfferService offerService ;
    protected final SubHandlerService subHandlerService;
    protected final EntityMapper entityMapper;
    protected final EmailTokenService emailTokenService ;
    protected final CreditService creditService ;
    protected final PasswordEncoder passwordEncoder;
    protected final PassAndUserRepository passAndUserRepository;

    @Autowired
    public BaseUserServiceImp(BaseUserRepository<T> baseUserRepository,PasswordEncoder passwordEncoder
            ,CreditService creditService,OrderService orderService,OfferService offerService
            ,SubHandlerService subHandlerService,EntityMapper entityMapper,EmailTokenService emailTokenService
            ,PassAndUserRepository passAndUserRepository)
    {
        this.baseUserRepository = baseUserRepository;
        this.orderService = orderService;
        this.offerService = offerService;
        this.subHandlerService = subHandlerService;
        this.entityMapper = entityMapper;
        this.emailTokenService = emailTokenService;
        this.creditService = creditService;
        this.passwordEncoder =passwordEncoder;
        this.passAndUserRepository = passAndUserRepository;
    }


    @Override
    public void validatePassWord(String pass) {
        String spaceRemovedPassword = pass.replaceAll("\\s","");
        checkIfIts8Digits(spaceRemovedPassword);
        checkIfAllNotBeLetterOrDigit(spaceRemovedPassword);
    }

    @Override
    @Transactional
    public void signUp(T t){
        baseUserRepository.save(t);
    }

    private void checkIfIts8Digits(String pass) throws PassNot8Digits{
        if (pass.length() != 8 ){
            throw new PassNot8Digits();
        }
    }
    private boolean checkIfAllNotBeNumber(String pass) throws PassNot8Digits{
        for (char c : pass.toCharArray()){
            if (!Character.isDigit(c)){
                return true;
            }
        }
        return false;
    }
    private boolean checkIfAllNotBeLetter(String pass) throws PassNot8Digits{
        for (char c : pass.toCharArray()){
            if (!Character.isLetter(c)){
                return true;
            }
        }
        return false;
    }
    private void checkIfAllNotBeLetterOrDigit(String pass){
        if (!(checkIfAllNotBeLetter(pass) && checkIfAllNotBeNumber(pass))){
            throw new AllNotBeLetterOrDigits();
        }
    }
    @Override
    @Transactional
    public String changingPassword(ChangingPasswordDto changingPasswordDto){
        validatePassWord(changingPasswordDto.newPass());
        PassAndUser managedPasswordAndUserNameFromDatabase =
                Optional.ofNullable(passAndUserRepository.findPass(changingPasswordDto.user(),
                String.valueOf(changingPasswordDto.typeOfUser()))).
                        orElseThrow(UnableToChangePassWord::new);
        if (!passwordEncoder.matches(changingPasswordDto.oldPass(), managedPasswordAndUserNameFromDatabase.getPass())){
            throw new PasswordNotCorrect();
        }
        managedPasswordAndUserNameFromDatabase.setPass(passwordEncoder.encode(changingPasswordDto.newPass()));
        passAndUserRepository.save(managedPasswordAndUserNameFromDatabase);
        return "successful";
    }

    @Override
    @Transactional
    public void updateUser(T t){
        baseUserRepository.update(t);
    }

    @Override
    @Transactional
    public T findById(int id , Class<T> tClass){
            return Optional.ofNullable(baseUserRepository.findById(id,tClass))
                    .orElseThrow(()-> new NotFoundUser("Unable to find Employee with this ID : "+ id));
    }

    @Override
    public List<Orders> optionalFindOrders(FindFilteredOrdersDto input){
        return orderService.optionalFindOrders(input.startDate(), input.endDate(), input.handlersName(), input.subHandlersName());
    }

}
