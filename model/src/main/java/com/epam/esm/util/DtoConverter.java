package com.epam.esm.util;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.GiftCertificateTagDto;
import com.epam.esm.dto.UserFormDto;
import com.epam.esm.dto.UserOrderDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificateTag;
import com.epam.esm.entity.User;
import com.epam.esm.entity.UserOrder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * DTO to Entity converter
 *
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DtoConverter {

    public static GiftCertificate convertCertificateToEntity(GiftCertificateDto dto) {
        return new GiftCertificate(
                dto.getName(),
                dto.getDescription(),
                BigDecimal.valueOf(dto.getPrice()),
                dto.getDuration(),
                convertTagSetToEntity(dto.getTags())
        );
    }

    public static GiftCertificate convertCertificateForOrderEntity(GiftCertificateDto dto) {
        GiftCertificate certificate = new GiftCertificate();
        certificate.setId(dto.getId());
        return certificate;
    }

    public static GiftCertificateTag convertTagToEntity(GiftCertificateTagDto tagDto) {
        return new GiftCertificateTag(
                tagDto.getName()
        );
    }

    public static User convertUserFormToEntity(UserFormDto form) {
        return new User(
                form.getUsername(),
                form.getPassword()
        );
    }

    public static Set<GiftCertificateTag> convertTagSetToEntity(Set<GiftCertificateTagDto> dtoTagSet){
        return dtoTagSet
                .stream()
                .map(DtoConverter::convertTagToEntity)
                .collect(Collectors.toSet());
    }

    public static UserOrder convertOrderToEntity(UserOrderDto orderDto){
        return new UserOrder(
                convertCertificateForOrderEntity(orderDto.getCertificate())
        );
    }
}
