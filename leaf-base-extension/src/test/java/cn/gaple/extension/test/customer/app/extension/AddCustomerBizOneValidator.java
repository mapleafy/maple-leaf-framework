package cn.gaple.extension.test.customer.app.extension;

import cn.gaple.extension.GXExtension;
import cn.gaple.extension.test.customer.app.extension.point.AddCustomerValidatorExtPoint;
import cn.gaple.extension.test.customer.client.AddCustomerCmd;
import cn.gaple.extension.test.customer.client.GXConstants;
import cn.gaple.extension.test.customer.domain.CustomerType;
import cn.maple.core.framework.exception.GXBusinessException;
import org.springframework.stereotype.Component;

@GXExtension(bizId = GXConstants.BIZ_1)
@Component
public class AddCustomerBizOneValidator implements AddCustomerValidatorExtPoint {
    public void validate(AddCustomerCmd addCustomerCmd) {
        //For BIZ TWO CustomerTYpe could not be VIP
        if (CustomerType.VIP == addCustomerCmd.getCustomerDTO().getCustomerType())
            throw new GXBusinessException("Customer Type could not be VIP for Biz One");
    }
}
