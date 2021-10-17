package com.java.springportfolio.factory;

import com.java.springportfolio.exception.ItemNotFoundException;
import com.java.springportfolio.service.OrderType;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static com.java.springportfolio.factory.SortFactory.*;

@UtilityClass
public class PageableFactory {

    public static Pageable getPageableWithSortingByPostCount(int pageNumber, int itemsPerPage) {
        Sort sort = getSortByNumberOfPosts();
        return PageRequest.of(pageNumber, itemsPerPage, sort);
    }

    private static Pageable getPageableWithSorting(int pageNumber, int itemsPerPage, Sort sort) {
        return PageRequest.of(pageNumber, itemsPerPage, sort);
    }

    public static Pageable getPageableWithSortingByDateAndVoteCount(int pageNumber, int itemsPerPage) {
        Sort sort = getSortByDateAndVoteCount();
        return PageRequest.of(pageNumber, itemsPerPage, sort);
    }

    private static Pageable getPageableWithSortingByDate(int pageNumber, int itemsPerPage) {
        Sort sortByDate = getSortByDate();
        return getPageableWithSorting(pageNumber, itemsPerPage, sortByDate);
    }

    private static Pageable getPageableWithSortingByVoteCount(int pageNumber, int itemsPerPage) {
        Sort sortByVoteCount = getSortByVoteCount();
        return getPageableWithSorting(pageNumber, itemsPerPage, sortByVoteCount);
    }

    public static Pageable getPageableByOrderType(OrderType orderType, int pageNumber, int itemsPerPage) {
        switch (orderType) {
            case NEW:
                return getPageableWithSortingByDate(pageNumber, itemsPerPage);
            case POPULAR:
                return getPageableWithSortingByVoteCount(pageNumber, itemsPerPage);
            default:
                throw new ItemNotFoundException("Order type has not been found");
        }
    }

    public static Pageable getTopicPageableByOrderType(OrderType orderType, int pageNumber, int itemsPerPage) {
        switch (orderType) {
            case NEW:
                return getPageableWithSortingByDate(pageNumber, itemsPerPage);
            case POPULAR:
                return getPageableWithSortingByPostCount(pageNumber, itemsPerPage);
            default:
                throw new ItemNotFoundException("Order type has not been found");
        }
    }
}
