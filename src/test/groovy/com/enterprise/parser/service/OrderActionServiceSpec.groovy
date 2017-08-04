package com.enterprise.parser.service

import com.enterprise.parser.Application
import com.enterprise.parser.model.Order
import com.enterprise.parser.store.StoreManager
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import spock.lang.Unroll

import javax.inject.Inject

@ContextConfiguration(classes = [Application])
class OrderActionServiceSpec extends Specification {

    @Inject
    StoreManager orderStoreManager

    @Inject
    OrderActionService orderActionService

    void setup() {
        orderStoreManager.reset()
    }

    @Unroll
    def 'New, Cancelled, Executed messages are received'() {
        when: 'a order is received'
        orderActionService.execute(message)

        then: 'check the order store'
        orderStoreManager.orderStore == orderStore
        orderStoreManager.volumeStore == volumeStore
        orderStoreManager.topVolumeStore.asList() == topVolumeStore

        where: 'the orders are as below'
        orderType  | message                                         | orderStore                              | volumeStore                    | topVolumeStore
        'ADD'      | '28961196AAK27GA0001BVS000500SPY   0001426400Y' | ['AK27GA0001BV': new Order('SPY', 500)] | ['SPY': new Order('SPY', 500)] | [new Order('SPY', 500)]
        'CANCEL'   | '28961196XAK27GA0001BV000100'                   | [:]                                     | [:]                            | []
        'EXECUTION' | '28961196EAK27GA0001BV000100XXX7GA000YYY'       | [:]                                     | [:]                            | []
    }

    def 'New order followed by Cancelled and Executed messages are received'() {
        when: 'a order is added'
        orderActionService.execute('28961196AAK27GA0001BVS000500SPY   0001426400Y')

        then: 'check the order store'
        orderStoreManager.orderStore == ['AK27GA0001BV': new Order('SPY', 500)]
        orderStoreManager.volumeStore == ['SPY': new Order('SPY', 500)]
        orderStoreManager.topVolumeStore.asList() == [new Order('SPY', 500)]

        when: 'a order is cancelled'
        orderActionService.execute('28961196XAK27GA0001BV000100')

        then: 'check the order store'
        orderStoreManager.orderStore == ['AK27GA0001BV': new Order('SPY', 400)]
        orderStoreManager.volumeStore == ['SPY': new Order('SPY', 600)]
        orderStoreManager.topVolumeStore.asList() == [new Order('SPY', 600)]

        when: 'a order is executed'
        orderActionService.execute('28961196EAK27GA0001BV000100XXX7GA000YYY')

        then: 'check the order store'
        orderStoreManager.orderStore == ['AK27GA0001BV': new Order('SPY', 300)]
        orderStoreManager.volumeStore == ['SPY': new Order('SPY', 700)]
        orderStoreManager.topVolumeStore.asList() == [new Order('SPY', 700)]

        when: 'the order is fully cancelled'
        orderActionService.execute('28961196XAK27GA0001BV000300')

        then: 'check the order store'
        orderStoreManager.orderStore.size() == 0
        orderStoreManager.volumeStore == ['SPY': new Order('SPY', 1000)]
        orderStoreManager.topVolumeStore.asList() == [new Order('SPY', 1000)]
    }
}
