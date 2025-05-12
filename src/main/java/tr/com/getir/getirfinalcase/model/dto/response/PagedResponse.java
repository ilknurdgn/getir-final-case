package tr.com.getir.getirfinalcase.model.dto.response;

import lombok.Builder;
import org.springframework.data.domain.Page;
import tr.com.getir.getirfinalcase.model.entity.Book;

import java.util.List;

@Builder
public record PagedResponse<T>(
        List<T> content,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean last
) {

    public static <T> PagedResponse<T> of(Page<T> page) {
        return new PagedResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
}
