package com.kotlinconf.workshop.channelsdemo

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
   val channel = Channel<Int>(Channel.RENDEZVOUS)

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
         delay(2000)
      }
   }
}

// Output:
//
// Sending 1...
// B received 1
// Sent 1.
// Sending 2...
// B received 2
// Sent 2.
// Sending 3...
// B received 3
// Sent 3.
// Sending 4...
// B received 4
// Sent 4.
// Sending 5...
// B received 5
// Sent 5.
// Sending 6...
// B received 6
// Sent 6.
// Sending 7...
// B received 7
// Sent 7.
// Sending 8...
// B received 8
// Sent 8.
//
