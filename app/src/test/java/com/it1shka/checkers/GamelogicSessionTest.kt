package com.it1shka.checkers

import com.it1shka.checkers.gamelogic.GameSession
import com.it1shka.checkers.gamelogic.intPairAsMove
import org.junit.Assert.*
import org.junit.Test

class SessionTest {
  @Test
  fun `session should be immutable`() {
    val session = GameSession.new()
    val initialHash = session.hashCode()
    val move = session.possibleMoves.random()
    val nextSession = session.makeMove(move)
    if (nextSession == null) {
      fail("After a possible move, session is null")
      return
    }
    val nextHash = nextSession.hashCode()
    assertNotEquals(initialHash, nextHash)
  }

  @Test
  fun `session should implement equality by value`() {
    var sessionA = GameSession.new()
    var sessionB = GameSession.new()
    assertEquals(sessionA, sessionB)
    assertEquals(sessionA.hashCode(), sessionB.hashCode())
    val move = (24 to 20).intPairAsMove!!
    sessionA = sessionA.makeMove(move)!!
    sessionB = sessionB.makeMove(move)!!
    assertEquals(sessionA, sessionB)
    assertEquals(sessionA.hashCode(), sessionB.hashCode())
  }

  @Test
  fun `different sessions should have different hash code`() {
    val session = GameSession.new()
    val nextSession = session.makeMove((24 to 20).intPairAsMove!!)!!
    assertNotEquals(session, nextSession)
    assertNotEquals(session.hashCode(), nextSession.hashCode())
  }
}