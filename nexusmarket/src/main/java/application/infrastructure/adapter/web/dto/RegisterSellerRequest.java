package application.infrastructure.adapter.web.dto;

/** Request to register a seller on behalf of an acting administrator. */
public record RegisterSellerRequest(Long actorId, Long userId, String businessInformation) {
}
