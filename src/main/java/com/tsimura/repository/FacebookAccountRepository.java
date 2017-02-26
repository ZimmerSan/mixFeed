package com.tsimura.repository;

import com.tsimura.domain.social.FacebookAccount;
import org.springframework.data.repository.CrudRepository;

public interface FacebookAccountRepository extends CrudRepository<FacebookAccount, String> {

    Iterable<FacebookAccount> findByOwner_Id(Long id);

}
