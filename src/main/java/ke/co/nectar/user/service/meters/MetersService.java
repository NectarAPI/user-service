package ke.co.nectar.user.service.meters;

import ke.co.nectar.user.entity.Meter;
import ke.co.nectar.user.entity.MeterType;
import ke.co.nectar.user.service.meters.impl.MetersServiceImpl;

import java.util.List;

public interface MetersService {

    MetersServiceImpl.CompositeMeterBundle add(MetersServiceImpl.MeterBundle meterBundle,
                                               String userRef) throws Exception;

    Meter findByRef(String ref) throws Exception;

    MetersServiceImpl.CompositeMeterBundle update(MetersServiceImpl.MeterBundle meter,
                                                  String meterRef, String userRef) throws Exception;

    List<MeterType> getMeterTypes() throws Exception;

    boolean deactivateMeter(String ref, String userRef) throws Exception;

    boolean activateMeter(String ref, String userRef) throws Exception;

}
