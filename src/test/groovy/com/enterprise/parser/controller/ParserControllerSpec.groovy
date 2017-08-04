package com.enterprise.parser.controller

import com.enterprise.parser.Application
import com.enterprise.parser.model.Order
import com.enterprise.parser.store.StoreManager
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import javax.inject.Inject

@ContextConfiguration(classes = [Application])
class ParserControllerSpec extends Specification {

    @Inject
    ParserController parserController

    @Inject
    StoreManager orderStoreManager

    def 'New, Cancelled, Execution messages are received'() {
        when: 'orders are received'
        parserController.parse(ParserControllerSpec.class.getResource('/test').path)

        then: 'check the order store'
        orderStoreManager.orderStore == ['AK27GA0001BV': new Order('SPY', 300)]
        orderStoreManager.volumeStore == ['SPY': new Order('SPY', 700)]
        orderStoreManager.topVolumeStore.asList() == [new Order('SPY', 700)]

    }
}
