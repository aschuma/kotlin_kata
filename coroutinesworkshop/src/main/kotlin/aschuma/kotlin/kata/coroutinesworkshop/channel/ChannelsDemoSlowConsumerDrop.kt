package com.kotlinconf.workshop.channelsdemo

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
   val channel =
      Channel<Int>(capacity = Channel.CONFLATED, onUndeliveredElement = { println("onUndeliveredElement: $it") })

   // Coroutine to send values to the channel
   launch {
      for (i in 1..8) {
         println("Sending $i...")
         channel.send(i)
         println("Sent $i.")
         delay(1000)
      }
      channel.close() // Close the channel when done sending
   }

   // Slow Consumer
   launch {
      for (value in channel) {
         println("B received $value")
         delay(3000)
      }
   }
}

// Output:
//
// Sending 1...
// Sent 1.
// B received 1
// Sending 2...
// Sent 2.
// Sending 3...
// onUndeliveredElement: 2
// Sent 3.
// B received 3
// Sending 4...
// Sent 4.
// Sending 5...
// onUndeliveredElement: 4
// Sent 5.
// Sending 6...
// onUndeliveredElement: 5
// Sent 6.
// B received 6
// Sending 7...
// Sent 7.
// Sending 8...
// onUndeliveredElement: 7
// Sent 8.
// B received 8
