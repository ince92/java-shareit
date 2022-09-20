package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;
import java.util.Optional;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    @Query("select i from ItemRequest i where i.requester.id <> ?1 order by i.created desc")
    List<ItemRequest> findAllItemRequest(long userId, Pageable pageRequest);

    Optional<ItemRequest> findItemRequestById(long requestId);

    List<ItemRequest> findItemRequestByRequesterIdOrderByCreatedDesc(long userId);
}
