package ke.co.nectar.user.service.permissions.impl;

import ke.co.nectar.user.entity.Permissions;
import ke.co.nectar.user.repository.PermissionsRepository;
import ke.co.nectar.user.service.permissions.PermissionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Service
public class PermissionsServiceImpl implements PermissionsService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PermissionsRepository permissionsRepository;

    @Override
    public List<Permissions> getPermissions() throws Exception {
        return permissionsRepository.findAll();
    }

}
