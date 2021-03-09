package com.epam.esm.util;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.GiftCertificateTagDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.UserOrderDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificateTag;
import com.epam.esm.entity.User;
import com.epam.esm.entity.UserOrder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Entity to DTO converter
 *
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EntityConverter {

    public static UserDto convertUserToDto(User userEntity) {
        return new UserDto(
                userEntity.getId(),
                userEntity.getName(),
                userEntity.getRole().name()
        );
    }

    public static UserOrderDto convertOrderToDto(UserOrder orderEntity) {
        return new UserOrderDto(orderEntity.getId(),
                orderEntity.getCost().doubleValue(),
                orderEntity.getPurchaseDate(),
                convertUserToDto(orderEntity.getUser()),
                convertCertificateToDto(orderEntity.getCertificate())
        );
    }

    public static GiftCertificateDto convertCertificateToDto(GiftCertificate certificate) {
        GiftCertificateDto dto = new GiftCertificateDto(
                certificate.getId(),
                certificate.getName(),
                certificate.getDescription(),
                certificate.getPrice().doubleValue(),
                certificate.getDuration()
        );
        dto.setCreateDate(certificate.getCreateDate());
        dto.setLastUpdateDate(certificate.getLastUpdateDate());
        dto.setTags(convertTagSetToDto(certificate.getTags()));
        return dto;
    }

    public static GiftCertificateTagDto convertTagToDto(GiftCertificateTag tag) {
        return new GiftCertificateTagDto(
                tag.getId(),
                tag.getName()
        );
    }

    public static Set<GiftCertificateTagDto> convertTagSetToDto(Set<GiftCertificateTag> tags) {
        return tags
                .stream()
                .map(EntityConverter::convertTagToDto)
                .collect(Collectors.toSet());
    }
}
