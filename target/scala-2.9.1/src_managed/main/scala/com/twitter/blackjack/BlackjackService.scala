package com.twitter.blackjack

import com.twitter.conversions.time._
import com.twitter.finagle.SourcedException
import com.twitter.scrooge.{ThriftStruct, ThriftStructCodec}
import com.twitter.util.Future
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import org.apache.thrift.protocol._
import org.apache.thrift.TApplicationException
import scala.collection.mutable
import scala.collection.{Map, Set}
import com.twitter.finagle.{Service => FinagleService}
import com.twitter.finagle.stats.{NullStatsReceiver, StatsReceiver}
import com.twitter.finagle.thrift.ThriftClientRequest
import com.twitter.finagle.{Service => FinagleService}
import java.util.Arrays
import org.apache.thrift.transport.{TMemoryBuffer, TMemoryInputTransport, TTransport}
import com.twitter.finagle.builder.{Server, ServerBuilder}
import com.twitter.finagle.stats.{StatsReceiver, OstrichStatsReceiver}
import com.twitter.finagle.thrift.ThriftServerFramedCodec
import com.twitter.finagle.tracing.{NullTracer, Tracer}
import com.twitter.logging.Logger
import com.twitter.ostrich.admin.Service
import com.twitter.util.Duration
import java.util.concurrent.atomic.AtomicReference


object BlackjackService {
  trait Iface {
    def startSession(`depositAmount`: Int): String
    def beginGame(`token`: String, `betAmount`: Int): Unit
    def getCurrentBet(`token`: String): Int
    def getGameStatus(`token`: String): Map[String, String]
    def hit(`token`: String): Unit
    def stand(`token`: String): Unit
    def getBalance(`token`: String): Int
    def endSession(`token`: String): Int
    def numPlayersOnline(): Int
  }

  trait FutureIface {
    def startSession(`depositAmount`: Int): Future[String]
    def beginGame(`token`: String, `betAmount`: Int): Future[Unit]
    def getCurrentBet(`token`: String): Future[Int]
    def getGameStatus(`token`: String): Future[Map[String, String]]
    def hit(`token`: String): Future[Unit]
    def stand(`token`: String): Future[Unit]
    def getBalance(`token`: String): Future[Int]
    def endSession(`token`: String): Future[Int]
    def numPlayersOnline(): Future[Int]
  }

  object startSession_args extends ThriftStructCodec[startSession_args] {
    val Struct = new TStruct("startSession_args")
    val DepositAmountField = new TField("depositAmount", TType.I32, 1)
  
    def encode(_item: startSession_args, _oproto: TProtocol) { _item.write(_oproto) }
    def decode(_iprot: TProtocol) = Immutable.decode(_iprot)
  
    def apply(_iprot: TProtocol): startSession_args = decode(_iprot)
  
    def apply(
      `depositAmount`: Int
    ): startSession_args = new Immutable(
      `depositAmount`
    )
  
    def unapply(_item: startSession_args): Option[Int] = Some(_item.depositAmount)
  
    object Immutable extends ThriftStructCodec[startSession_args] {
      def encode(_item: startSession_args, _oproto: TProtocol) { _item.write(_oproto) }
      def decode(_iprot: TProtocol) = {
        var `depositAmount`: Int = 0
        var _got_depositAmount = false
        var _done = false
        _iprot.readStructBegin()
        while (!_done) {
          val _field = _iprot.readFieldBegin()
          if (_field.`type` == TType.STOP) {
            _done = true
          } else {
            _field.id match {
              case 1 => { /* depositAmount */
                _field.`type` match {
                  case TType.I32 => {
                    `depositAmount` = {
                      _iprot.readI32()
                    }
                    _got_depositAmount = true
                  }
                  case _ => TProtocolUtil.skip(_iprot, _field.`type`)
                }
              }
              case _ => TProtocolUtil.skip(_iprot, _field.`type`)
            }
            _iprot.readFieldEnd()
          }
        }
        _iprot.readStructEnd()
        new Immutable(
          `depositAmount`
        )
      }
    }
  
    /**
     * The default read-only implementation of startSession_args.  You typically should not need to
     * directly reference this class; instead, use the startSession_args.apply method to construct
     * new instances.
     */
    class Immutable(
      val `depositAmount`: Int
    ) extends startSession_args
  
  }
  
  trait startSession_args extends ThriftStruct
    with Product1[Int]
    with java.io.Serializable
  {
    import startSession_args._
  
    def `depositAmount`: Int
  
    def _1 = `depositAmount`
  
    override def write(_oprot: TProtocol) {
      validate()
      _oprot.writeStructBegin(Struct)
      if (true) {
        val `depositAmount_item` = `depositAmount`
        _oprot.writeFieldBegin(DepositAmountField)
        _oprot.writeI32(`depositAmount_item`)
        _oprot.writeFieldEnd()
      }
      _oprot.writeFieldStop()
      _oprot.writeStructEnd()
    }
  
    def copy(
      `depositAmount`: Int = this.`depositAmount`
    ): startSession_args = new Immutable(
      `depositAmount`
    )
  
    /**
     * Checks that all required fields are non-null.
     */
    def validate() {
    }
  
    def canEqual(other: Any) = other.isInstanceOf[startSession_args]
  
    override def equals(other: Any): Boolean = runtime.ScalaRunTime._equals(this, other)
  
    override def hashCode: Int = runtime.ScalaRunTime._hashCode(this)
  
    override def toString: String = runtime.ScalaRunTime._toString(this)
  
    override def productArity = 1
  
    override def productElement(n: Int): Any = n match {
      case 0 => `depositAmount`
      case _ => throw new IndexOutOfBoundsException(n.toString)
    }
  
    override def productPrefix = "startSession_args"
  }
  object startSession_result extends ThriftStructCodec[startSession_result] {
    val Struct = new TStruct("startSession_result")
    val SuccessField = new TField("success", TType.STRING, 0)
  
    def encode(_item: startSession_result, _oproto: TProtocol) { _item.write(_oproto) }
    def decode(_iprot: TProtocol) = Immutable.decode(_iprot)
  
    def apply(_iprot: TProtocol): startSession_result = decode(_iprot)
  
    def apply(
      `success`: Option[String] = None
    ): startSession_result = new Immutable(
      `success`
    )
  
    def unapply(_item: startSession_result): Option[Option[String]] = Some(_item.success)
  
    object Immutable extends ThriftStructCodec[startSession_result] {
      def encode(_item: startSession_result, _oproto: TProtocol) { _item.write(_oproto) }
      def decode(_iprot: TProtocol) = {
        var `success`: String = null
        var _got_success = false
        var _done = false
        _iprot.readStructBegin()
        while (!_done) {
          val _field = _iprot.readFieldBegin()
          if (_field.`type` == TType.STOP) {
            _done = true
          } else {
            _field.id match {
              case 0 => { /* success */
                _field.`type` match {
                  case TType.STRING => {
                    `success` = {
                      _iprot.readString()
                    }
                    _got_success = true
                  }
                  case _ => TProtocolUtil.skip(_iprot, _field.`type`)
                }
              }
              case _ => TProtocolUtil.skip(_iprot, _field.`type`)
            }
            _iprot.readFieldEnd()
          }
        }
        _iprot.readStructEnd()
        new Immutable(
          if (_got_success) Some(`success`) else None
        )
      }
    }
  
    /**
     * The default read-only implementation of startSession_result.  You typically should not need to
     * directly reference this class; instead, use the startSession_result.apply method to construct
     * new instances.
     */
    class Immutable(
      val `success`: Option[String] = None
    ) extends startSession_result
  
  }
  
  trait startSession_result extends ThriftStruct
    with Product1[Option[String]]
    with java.io.Serializable
  {
    import startSession_result._
  
    def `success`: Option[String]
  
    def _1 = `success`
  
    override def write(_oprot: TProtocol) {
      validate()
      _oprot.writeStructBegin(Struct)
      if (`success`.isDefined) {
        val `success_item` = `success`.get
        _oprot.writeFieldBegin(SuccessField)
        _oprot.writeString(`success_item`)
        _oprot.writeFieldEnd()
      }
      _oprot.writeFieldStop()
      _oprot.writeStructEnd()
    }
  
    def copy(
      `success`: Option[String] = this.`success`
    ): startSession_result = new Immutable(
      `success`
    )
  
    /**
     * Checks that all required fields are non-null.
     */
    def validate() {
    }
  
    def canEqual(other: Any) = other.isInstanceOf[startSession_result]
  
    override def equals(other: Any): Boolean = runtime.ScalaRunTime._equals(this, other)
  
    override def hashCode: Int = runtime.ScalaRunTime._hashCode(this)
  
    override def toString: String = runtime.ScalaRunTime._toString(this)
  
    override def productArity = 1
  
    override def productElement(n: Int): Any = n match {
      case 0 => `success`
      case _ => throw new IndexOutOfBoundsException(n.toString)
    }
  
    override def productPrefix = "startSession_result"
  }
  object beginGame_args extends ThriftStructCodec[beginGame_args] {
    val Struct = new TStruct("beginGame_args")
    val TokenField = new TField("token", TType.STRING, 1)
    val BetAmountField = new TField("betAmount", TType.I32, 2)
  
    def encode(_item: beginGame_args, _oproto: TProtocol) { _item.write(_oproto) }
    def decode(_iprot: TProtocol) = Immutable.decode(_iprot)
  
    def apply(_iprot: TProtocol): beginGame_args = decode(_iprot)
  
    def apply(
      `token`: String,
      `betAmount`: Int
    ): beginGame_args = new Immutable(
      `token`,
      `betAmount`
    )
  
    def unapply(_item: beginGame_args): Option[Product2[String, Int]] = Some(_item)
  
    object Immutable extends ThriftStructCodec[beginGame_args] {
      def encode(_item: beginGame_args, _oproto: TProtocol) { _item.write(_oproto) }
      def decode(_iprot: TProtocol) = {
        var `token`: String = null
        var _got_token = false
        var `betAmount`: Int = 0
        var _got_betAmount = false
        var _done = false
        _iprot.readStructBegin()
        while (!_done) {
          val _field = _iprot.readFieldBegin()
          if (_field.`type` == TType.STOP) {
            _done = true
          } else {
            _field.id match {
              case 1 => { /* token */
                _field.`type` match {
                  case TType.STRING => {
                    `token` = {
                      _iprot.readString()
                    }
                    _got_token = true
                  }
                  case _ => TProtocolUtil.skip(_iprot, _field.`type`)
                }
              }
              case 2 => { /* betAmount */
                _field.`type` match {
                  case TType.I32 => {
                    `betAmount` = {
                      _iprot.readI32()
                    }
                    _got_betAmount = true
                  }
                  case _ => TProtocolUtil.skip(_iprot, _field.`type`)
                }
              }
              case _ => TProtocolUtil.skip(_iprot, _field.`type`)
            }
            _iprot.readFieldEnd()
          }
        }
        _iprot.readStructEnd()
        new Immutable(
          `token`,
          `betAmount`
        )
      }
    }
  
    /**
     * The default read-only implementation of beginGame_args.  You typically should not need to
     * directly reference this class; instead, use the beginGame_args.apply method to construct
     * new instances.
     */
    class Immutable(
      val `token`: String,
      val `betAmount`: Int
    ) extends beginGame_args
  
  }
  
  trait beginGame_args extends ThriftStruct
    with Product2[String, Int]
    with java.io.Serializable
  {
    import beginGame_args._
  
    def `token`: String
    def `betAmount`: Int
  
    def _1 = `token`
    def _2 = `betAmount`
  
    override def write(_oprot: TProtocol) {
      validate()
      _oprot.writeStructBegin(Struct)
      if (true) {
        val `token_item` = `token`
        _oprot.writeFieldBegin(TokenField)
        _oprot.writeString(`token_item`)
        _oprot.writeFieldEnd()
      }
      if (true) {
        val `betAmount_item` = `betAmount`
        _oprot.writeFieldBegin(BetAmountField)
        _oprot.writeI32(`betAmount_item`)
        _oprot.writeFieldEnd()
      }
      _oprot.writeFieldStop()
      _oprot.writeStructEnd()
    }
  
    def copy(
      `token`: String = this.`token`,
      `betAmount`: Int = this.`betAmount`
    ): beginGame_args = new Immutable(
      `token`,
      `betAmount`
    )
  
    /**
     * Checks that all required fields are non-null.
     */
    def validate() {
    }
  
    def canEqual(other: Any) = other.isInstanceOf[beginGame_args]
  
    override def equals(other: Any): Boolean = runtime.ScalaRunTime._equals(this, other)
  
    override def hashCode: Int = runtime.ScalaRunTime._hashCode(this)
  
    override def toString: String = runtime.ScalaRunTime._toString(this)
  
    override def productArity = 2
  
    override def productElement(n: Int): Any = n match {
      case 0 => `token`
      case 1 => `betAmount`
      case _ => throw new IndexOutOfBoundsException(n.toString)
    }
  
    override def productPrefix = "beginGame_args"
  }
  object beginGame_result extends ThriftStructCodec[beginGame_result] {
    val Struct = new TStruct("beginGame_result")
  
    def encode(_item: beginGame_result, _oproto: TProtocol) { _item.write(_oproto) }
    def decode(_iprot: TProtocol) = Immutable.decode(_iprot)
  
    def apply(_iprot: TProtocol): beginGame_result = decode(_iprot)
  
    def apply(
    ): beginGame_result = new Immutable(
    )
  
    def unapply(_item: beginGame_result): Boolean = true
  
    object Immutable extends ThriftStructCodec[beginGame_result] {
      def encode(_item: beginGame_result, _oproto: TProtocol) { _item.write(_oproto) }
      def decode(_iprot: TProtocol) = {
        var _done = false
        _iprot.readStructBegin()
        while (!_done) {
          val _field = _iprot.readFieldBegin()
          if (_field.`type` == TType.STOP) {
            _done = true
          } else {
            _field.id match {
              case _ => TProtocolUtil.skip(_iprot, _field.`type`)
            }
            _iprot.readFieldEnd()
          }
        }
        _iprot.readStructEnd()
        new Immutable(
        )
      }
    }
  
    /**
     * The default read-only implementation of beginGame_result.  You typically should not need to
     * directly reference this class; instead, use the beginGame_result.apply method to construct
     * new instances.
     */
    class Immutable(
    ) extends beginGame_result
  
  }
  
  trait beginGame_result extends ThriftStruct
    with Product
    with java.io.Serializable
  {
    import beginGame_result._
  
  
  
    override def write(_oprot: TProtocol) {
      validate()
      _oprot.writeStructBegin(Struct)
      _oprot.writeFieldStop()
      _oprot.writeStructEnd()
    }
  
    def copy(
    ): beginGame_result = new Immutable(
    )
  
    /**
     * Checks that all required fields are non-null.
     */
    def validate() {
    }
  
    def canEqual(other: Any) = other.isInstanceOf[beginGame_result]
  
    override def equals(other: Any): Boolean = runtime.ScalaRunTime._equals(this, other)
  
    override def hashCode: Int = runtime.ScalaRunTime._hashCode(this)
  
    override def toString: String = runtime.ScalaRunTime._toString(this)
  
    override def productArity = 0
  
    override def productElement(n: Int): Any = n match {
      case _ => throw new IndexOutOfBoundsException(n.toString)
    }
  
    override def productPrefix = "beginGame_result"
  }
  object getCurrentBet_args extends ThriftStructCodec[getCurrentBet_args] {
    val Struct = new TStruct("getCurrentBet_args")
    val TokenField = new TField("token", TType.STRING, 1)
  
    def encode(_item: getCurrentBet_args, _oproto: TProtocol) { _item.write(_oproto) }
    def decode(_iprot: TProtocol) = Immutable.decode(_iprot)
  
    def apply(_iprot: TProtocol): getCurrentBet_args = decode(_iprot)
  
    def apply(
      `token`: String
    ): getCurrentBet_args = new Immutable(
      `token`
    )
  
    def unapply(_item: getCurrentBet_args): Option[String] = Some(_item.token)
  
    object Immutable extends ThriftStructCodec[getCurrentBet_args] {
      def encode(_item: getCurrentBet_args, _oproto: TProtocol) { _item.write(_oproto) }
      def decode(_iprot: TProtocol) = {
        var `token`: String = null
        var _got_token = false
        var _done = false
        _iprot.readStructBegin()
        while (!_done) {
          val _field = _iprot.readFieldBegin()
          if (_field.`type` == TType.STOP) {
            _done = true
          } else {
            _field.id match {
              case 1 => { /* token */
                _field.`type` match {
                  case TType.STRING => {
                    `token` = {
                      _iprot.readString()
                    }
                    _got_token = true
                  }
                  case _ => TProtocolUtil.skip(_iprot, _field.`type`)
                }
              }
              case _ => TProtocolUtil.skip(_iprot, _field.`type`)
            }
            _iprot.readFieldEnd()
          }
        }
        _iprot.readStructEnd()
        new Immutable(
          `token`
        )
      }
    }
  
    /**
     * The default read-only implementation of getCurrentBet_args.  You typically should not need to
     * directly reference this class; instead, use the getCurrentBet_args.apply method to construct
     * new instances.
     */
    class Immutable(
      val `token`: String
    ) extends getCurrentBet_args
  
  }
  
  trait getCurrentBet_args extends ThriftStruct
    with Product1[String]
    with java.io.Serializable
  {
    import getCurrentBet_args._
  
    def `token`: String
  
    def _1 = `token`
  
    override def write(_oprot: TProtocol) {
      validate()
      _oprot.writeStructBegin(Struct)
      if (true) {
        val `token_item` = `token`
        _oprot.writeFieldBegin(TokenField)
        _oprot.writeString(`token_item`)
        _oprot.writeFieldEnd()
      }
      _oprot.writeFieldStop()
      _oprot.writeStructEnd()
    }
  
    def copy(
      `token`: String = this.`token`
    ): getCurrentBet_args = new Immutable(
      `token`
    )
  
    /**
     * Checks that all required fields are non-null.
     */
    def validate() {
    }
  
    def canEqual(other: Any) = other.isInstanceOf[getCurrentBet_args]
  
    override def equals(other: Any): Boolean = runtime.ScalaRunTime._equals(this, other)
  
    override def hashCode: Int = runtime.ScalaRunTime._hashCode(this)
  
    override def toString: String = runtime.ScalaRunTime._toString(this)
  
    override def productArity = 1
  
    override def productElement(n: Int): Any = n match {
      case 0 => `token`
      case _ => throw new IndexOutOfBoundsException(n.toString)
    }
  
    override def productPrefix = "getCurrentBet_args"
  }
  object getCurrentBet_result extends ThriftStructCodec[getCurrentBet_result] {
    val Struct = new TStruct("getCurrentBet_result")
    val SuccessField = new TField("success", TType.I32, 0)
  
    def encode(_item: getCurrentBet_result, _oproto: TProtocol) { _item.write(_oproto) }
    def decode(_iprot: TProtocol) = Immutable.decode(_iprot)
  
    def apply(_iprot: TProtocol): getCurrentBet_result = decode(_iprot)
  
    def apply(
      `success`: Option[Int] = None
    ): getCurrentBet_result = new Immutable(
      `success`
    )
  
    def unapply(_item: getCurrentBet_result): Option[Option[Int]] = Some(_item.success)
  
    object Immutable extends ThriftStructCodec[getCurrentBet_result] {
      def encode(_item: getCurrentBet_result, _oproto: TProtocol) { _item.write(_oproto) }
      def decode(_iprot: TProtocol) = {
        var `success`: Int = 0
        var _got_success = false
        var _done = false
        _iprot.readStructBegin()
        while (!_done) {
          val _field = _iprot.readFieldBegin()
          if (_field.`type` == TType.STOP) {
            _done = true
          } else {
            _field.id match {
              case 0 => { /* success */
                _field.`type` match {
                  case TType.I32 => {
                    `success` = {
                      _iprot.readI32()
                    }
                    _got_success = true
                  }
                  case _ => TProtocolUtil.skip(_iprot, _field.`type`)
                }
              }
              case _ => TProtocolUtil.skip(_iprot, _field.`type`)
            }
            _iprot.readFieldEnd()
          }
        }
        _iprot.readStructEnd()
        new Immutable(
          if (_got_success) Some(`success`) else None
        )
      }
    }
  
    /**
     * The default read-only implementation of getCurrentBet_result.  You typically should not need to
     * directly reference this class; instead, use the getCurrentBet_result.apply method to construct
     * new instances.
     */
    class Immutable(
      val `success`: Option[Int] = None
    ) extends getCurrentBet_result
  
  }
  
  trait getCurrentBet_result extends ThriftStruct
    with Product1[Option[Int]]
    with java.io.Serializable
  {
    import getCurrentBet_result._
  
    def `success`: Option[Int]
  
    def _1 = `success`
  
    override def write(_oprot: TProtocol) {
      validate()
      _oprot.writeStructBegin(Struct)
      if (`success`.isDefined) {
        val `success_item` = `success`.get
        _oprot.writeFieldBegin(SuccessField)
        _oprot.writeI32(`success_item`)
        _oprot.writeFieldEnd()
      }
      _oprot.writeFieldStop()
      _oprot.writeStructEnd()
    }
  
    def copy(
      `success`: Option[Int] = this.`success`
    ): getCurrentBet_result = new Immutable(
      `success`
    )
  
    /**
     * Checks that all required fields are non-null.
     */
    def validate() {
    }
  
    def canEqual(other: Any) = other.isInstanceOf[getCurrentBet_result]
  
    override def equals(other: Any): Boolean = runtime.ScalaRunTime._equals(this, other)
  
    override def hashCode: Int = runtime.ScalaRunTime._hashCode(this)
  
    override def toString: String = runtime.ScalaRunTime._toString(this)
  
    override def productArity = 1
  
    override def productElement(n: Int): Any = n match {
      case 0 => `success`
      case _ => throw new IndexOutOfBoundsException(n.toString)
    }
  
    override def productPrefix = "getCurrentBet_result"
  }
  object getGameStatus_args extends ThriftStructCodec[getGameStatus_args] {
    val Struct = new TStruct("getGameStatus_args")
    val TokenField = new TField("token", TType.STRING, 1)
  
    def encode(_item: getGameStatus_args, _oproto: TProtocol) { _item.write(_oproto) }
    def decode(_iprot: TProtocol) = Immutable.decode(_iprot)
  
    def apply(_iprot: TProtocol): getGameStatus_args = decode(_iprot)
  
    def apply(
      `token`: String
    ): getGameStatus_args = new Immutable(
      `token`
    )
  
    def unapply(_item: getGameStatus_args): Option[String] = Some(_item.token)
  
    object Immutable extends ThriftStructCodec[getGameStatus_args] {
      def encode(_item: getGameStatus_args, _oproto: TProtocol) { _item.write(_oproto) }
      def decode(_iprot: TProtocol) = {
        var `token`: String = null
        var _got_token = false
        var _done = false
        _iprot.readStructBegin()
        while (!_done) {
          val _field = _iprot.readFieldBegin()
          if (_field.`type` == TType.STOP) {
            _done = true
          } else {
            _field.id match {
              case 1 => { /* token */
                _field.`type` match {
                  case TType.STRING => {
                    `token` = {
                      _iprot.readString()
                    }
                    _got_token = true
                  }
                  case _ => TProtocolUtil.skip(_iprot, _field.`type`)
                }
              }
              case _ => TProtocolUtil.skip(_iprot, _field.`type`)
            }
            _iprot.readFieldEnd()
          }
        }
        _iprot.readStructEnd()
        new Immutable(
          `token`
        )
      }
    }
  
    /**
     * The default read-only implementation of getGameStatus_args.  You typically should not need to
     * directly reference this class; instead, use the getGameStatus_args.apply method to construct
     * new instances.
     */
    class Immutable(
      val `token`: String
    ) extends getGameStatus_args
  
  }
  
  trait getGameStatus_args extends ThriftStruct
    with Product1[String]
    with java.io.Serializable
  {
    import getGameStatus_args._
  
    def `token`: String
  
    def _1 = `token`
  
    override def write(_oprot: TProtocol) {
      validate()
      _oprot.writeStructBegin(Struct)
      if (true) {
        val `token_item` = `token`
        _oprot.writeFieldBegin(TokenField)
        _oprot.writeString(`token_item`)
        _oprot.writeFieldEnd()
      }
      _oprot.writeFieldStop()
      _oprot.writeStructEnd()
    }
  
    def copy(
      `token`: String = this.`token`
    ): getGameStatus_args = new Immutable(
      `token`
    )
  
    /**
     * Checks that all required fields are non-null.
     */
    def validate() {
    }
  
    def canEqual(other: Any) = other.isInstanceOf[getGameStatus_args]
  
    override def equals(other: Any): Boolean = runtime.ScalaRunTime._equals(this, other)
  
    override def hashCode: Int = runtime.ScalaRunTime._hashCode(this)
  
    override def toString: String = runtime.ScalaRunTime._toString(this)
  
    override def productArity = 1
  
    override def productElement(n: Int): Any = n match {
      case 0 => `token`
      case _ => throw new IndexOutOfBoundsException(n.toString)
    }
  
    override def productPrefix = "getGameStatus_args"
  }
  object getGameStatus_result extends ThriftStructCodec[getGameStatus_result] {
    val Struct = new TStruct("getGameStatus_result")
    val SuccessField = new TField("success", TType.MAP, 0)
  
    def encode(_item: getGameStatus_result, _oproto: TProtocol) { _item.write(_oproto) }
    def decode(_iprot: TProtocol) = Immutable.decode(_iprot)
  
    def apply(_iprot: TProtocol): getGameStatus_result = decode(_iprot)
  
    def apply(
      `success`: Option[Map[String, String]] = None
    ): getGameStatus_result = new Immutable(
      `success`
    )
  
    def unapply(_item: getGameStatus_result): Option[Option[Map[String, String]]] = Some(_item.success)
  
    object Immutable extends ThriftStructCodec[getGameStatus_result] {
      def encode(_item: getGameStatus_result, _oproto: TProtocol) { _item.write(_oproto) }
      def decode(_iprot: TProtocol) = {
        var `success`: Map[String, String] = Map[String, String]()
        var _got_success = false
        var _done = false
        _iprot.readStructBegin()
        while (!_done) {
          val _field = _iprot.readFieldBegin()
          if (_field.`type` == TType.STOP) {
            _done = true
          } else {
            _field.id match {
              case 0 => { /* success */
                _field.`type` match {
                  case TType.MAP => {
                    `success` = {
                      val _map = _iprot.readMapBegin()
                      val _rv = new mutable.HashMap[String, String]
                      var _i = 0
                      while (_i < _map.size) {
                        val _key = {
                          _iprot.readString()
                        }
                        val _value = {
                          _iprot.readString()
                        }
                        _rv(_key) = _value
                        _i += 1
                      }
                      _iprot.readMapEnd()
                      _rv
                    }
                    _got_success = true
                  }
                  case _ => TProtocolUtil.skip(_iprot, _field.`type`)
                }
              }
              case _ => TProtocolUtil.skip(_iprot, _field.`type`)
            }
            _iprot.readFieldEnd()
          }
        }
        _iprot.readStructEnd()
        new Immutable(
          if (_got_success) Some(`success`) else None
        )
      }
    }
  
    /**
     * The default read-only implementation of getGameStatus_result.  You typically should not need to
     * directly reference this class; instead, use the getGameStatus_result.apply method to construct
     * new instances.
     */
    class Immutable(
      val `success`: Option[Map[String, String]] = None
    ) extends getGameStatus_result
  
  }
  
  trait getGameStatus_result extends ThriftStruct
    with Product1[Option[Map[String, String]]]
    with java.io.Serializable
  {
    import getGameStatus_result._
  
    def `success`: Option[Map[String, String]]
  
    def _1 = `success`
  
    override def write(_oprot: TProtocol) {
      validate()
      _oprot.writeStructBegin(Struct)
      if (`success`.isDefined) {
        val `success_item` = `success`.get
        _oprot.writeFieldBegin(SuccessField)
        _oprot.writeMapBegin(new TMap(TType.STRING, TType.STRING, `success_item`.size))
        `success_item`.foreach { _pair =>
          val `_success_item_key` = _pair._1
          val `_success_item_value` = _pair._2
          _oprot.writeString(`_success_item_key`)
          _oprot.writeString(`_success_item_value`)
        }
        _oprot.writeMapEnd()
        _oprot.writeFieldEnd()
      }
      _oprot.writeFieldStop()
      _oprot.writeStructEnd()
    }
  
    def copy(
      `success`: Option[Map[String, String]] = this.`success`
    ): getGameStatus_result = new Immutable(
      `success`
    )
  
    /**
     * Checks that all required fields are non-null.
     */
    def validate() {
    }
  
    def canEqual(other: Any) = other.isInstanceOf[getGameStatus_result]
  
    override def equals(other: Any): Boolean = runtime.ScalaRunTime._equals(this, other)
  
    override def hashCode: Int = runtime.ScalaRunTime._hashCode(this)
  
    override def toString: String = runtime.ScalaRunTime._toString(this)
  
    override def productArity = 1
  
    override def productElement(n: Int): Any = n match {
      case 0 => `success`
      case _ => throw new IndexOutOfBoundsException(n.toString)
    }
  
    override def productPrefix = "getGameStatus_result"
  }
  object hit_args extends ThriftStructCodec[hit_args] {
    val Struct = new TStruct("hit_args")
    val TokenField = new TField("token", TType.STRING, 1)
  
    def encode(_item: hit_args, _oproto: TProtocol) { _item.write(_oproto) }
    def decode(_iprot: TProtocol) = Immutable.decode(_iprot)
  
    def apply(_iprot: TProtocol): hit_args = decode(_iprot)
  
    def apply(
      `token`: String
    ): hit_args = new Immutable(
      `token`
    )
  
    def unapply(_item: hit_args): Option[String] = Some(_item.token)
  
    object Immutable extends ThriftStructCodec[hit_args] {
      def encode(_item: hit_args, _oproto: TProtocol) { _item.write(_oproto) }
      def decode(_iprot: TProtocol) = {
        var `token`: String = null
        var _got_token = false
        var _done = false
        _iprot.readStructBegin()
        while (!_done) {
          val _field = _iprot.readFieldBegin()
          if (_field.`type` == TType.STOP) {
            _done = true
          } else {
            _field.id match {
              case 1 => { /* token */
                _field.`type` match {
                  case TType.STRING => {
                    `token` = {
                      _iprot.readString()
                    }
                    _got_token = true
                  }
                  case _ => TProtocolUtil.skip(_iprot, _field.`type`)
                }
              }
              case _ => TProtocolUtil.skip(_iprot, _field.`type`)
            }
            _iprot.readFieldEnd()
          }
        }
        _iprot.readStructEnd()
        new Immutable(
          `token`
        )
      }
    }
  
    /**
     * The default read-only implementation of hit_args.  You typically should not need to
     * directly reference this class; instead, use the hit_args.apply method to construct
     * new instances.
     */
    class Immutable(
      val `token`: String
    ) extends hit_args
  
  }
  
  trait hit_args extends ThriftStruct
    with Product1[String]
    with java.io.Serializable
  {
    import hit_args._
  
    def `token`: String
  
    def _1 = `token`
  
    override def write(_oprot: TProtocol) {
      validate()
      _oprot.writeStructBegin(Struct)
      if (true) {
        val `token_item` = `token`
        _oprot.writeFieldBegin(TokenField)
        _oprot.writeString(`token_item`)
        _oprot.writeFieldEnd()
      }
      _oprot.writeFieldStop()
      _oprot.writeStructEnd()
    }
  
    def copy(
      `token`: String = this.`token`
    ): hit_args = new Immutable(
      `token`
    )
  
    /**
     * Checks that all required fields are non-null.
     */
    def validate() {
    }
  
    def canEqual(other: Any) = other.isInstanceOf[hit_args]
  
    override def equals(other: Any): Boolean = runtime.ScalaRunTime._equals(this, other)
  
    override def hashCode: Int = runtime.ScalaRunTime._hashCode(this)
  
    override def toString: String = runtime.ScalaRunTime._toString(this)
  
    override def productArity = 1
  
    override def productElement(n: Int): Any = n match {
      case 0 => `token`
      case _ => throw new IndexOutOfBoundsException(n.toString)
    }
  
    override def productPrefix = "hit_args"
  }
  object hit_result extends ThriftStructCodec[hit_result] {
    val Struct = new TStruct("hit_result")
  
    def encode(_item: hit_result, _oproto: TProtocol) { _item.write(_oproto) }
    def decode(_iprot: TProtocol) = Immutable.decode(_iprot)
  
    def apply(_iprot: TProtocol): hit_result = decode(_iprot)
  
    def apply(
    ): hit_result = new Immutable(
    )
  
    def unapply(_item: hit_result): Boolean = true
  
    object Immutable extends ThriftStructCodec[hit_result] {
      def encode(_item: hit_result, _oproto: TProtocol) { _item.write(_oproto) }
      def decode(_iprot: TProtocol) = {
        var _done = false
        _iprot.readStructBegin()
        while (!_done) {
          val _field = _iprot.readFieldBegin()
          if (_field.`type` == TType.STOP) {
            _done = true
          } else {
            _field.id match {
              case _ => TProtocolUtil.skip(_iprot, _field.`type`)
            }
            _iprot.readFieldEnd()
          }
        }
        _iprot.readStructEnd()
        new Immutable(
        )
      }
    }
  
    /**
     * The default read-only implementation of hit_result.  You typically should not need to
     * directly reference this class; instead, use the hit_result.apply method to construct
     * new instances.
     */
    class Immutable(
    ) extends hit_result
  
  }
  
  trait hit_result extends ThriftStruct
    with Product
    with java.io.Serializable
  {
    import hit_result._
  
  
  
    override def write(_oprot: TProtocol) {
      validate()
      _oprot.writeStructBegin(Struct)
      _oprot.writeFieldStop()
      _oprot.writeStructEnd()
    }
  
    def copy(
    ): hit_result = new Immutable(
    )
  
    /**
     * Checks that all required fields are non-null.
     */
    def validate() {
    }
  
    def canEqual(other: Any) = other.isInstanceOf[hit_result]
  
    override def equals(other: Any): Boolean = runtime.ScalaRunTime._equals(this, other)
  
    override def hashCode: Int = runtime.ScalaRunTime._hashCode(this)
  
    override def toString: String = runtime.ScalaRunTime._toString(this)
  
    override def productArity = 0
  
    override def productElement(n: Int): Any = n match {
      case _ => throw new IndexOutOfBoundsException(n.toString)
    }
  
    override def productPrefix = "hit_result"
  }
  object stand_args extends ThriftStructCodec[stand_args] {
    val Struct = new TStruct("stand_args")
    val TokenField = new TField("token", TType.STRING, 1)
  
    def encode(_item: stand_args, _oproto: TProtocol) { _item.write(_oproto) }
    def decode(_iprot: TProtocol) = Immutable.decode(_iprot)
  
    def apply(_iprot: TProtocol): stand_args = decode(_iprot)
  
    def apply(
      `token`: String
    ): stand_args = new Immutable(
      `token`
    )
  
    def unapply(_item: stand_args): Option[String] = Some(_item.token)
  
    object Immutable extends ThriftStructCodec[stand_args] {
      def encode(_item: stand_args, _oproto: TProtocol) { _item.write(_oproto) }
      def decode(_iprot: TProtocol) = {
        var `token`: String = null
        var _got_token = false
        var _done = false
        _iprot.readStructBegin()
        while (!_done) {
          val _field = _iprot.readFieldBegin()
          if (_field.`type` == TType.STOP) {
            _done = true
          } else {
            _field.id match {
              case 1 => { /* token */
                _field.`type` match {
                  case TType.STRING => {
                    `token` = {
                      _iprot.readString()
                    }
                    _got_token = true
                  }
                  case _ => TProtocolUtil.skip(_iprot, _field.`type`)
                }
              }
              case _ => TProtocolUtil.skip(_iprot, _field.`type`)
            }
            _iprot.readFieldEnd()
          }
        }
        _iprot.readStructEnd()
        new Immutable(
          `token`
        )
      }
    }
  
    /**
     * The default read-only implementation of stand_args.  You typically should not need to
     * directly reference this class; instead, use the stand_args.apply method to construct
     * new instances.
     */
    class Immutable(
      val `token`: String
    ) extends stand_args
  
  }
  
  trait stand_args extends ThriftStruct
    with Product1[String]
    with java.io.Serializable
  {
    import stand_args._
  
    def `token`: String
  
    def _1 = `token`
  
    override def write(_oprot: TProtocol) {
      validate()
      _oprot.writeStructBegin(Struct)
      if (true) {
        val `token_item` = `token`
        _oprot.writeFieldBegin(TokenField)
        _oprot.writeString(`token_item`)
        _oprot.writeFieldEnd()
      }
      _oprot.writeFieldStop()
      _oprot.writeStructEnd()
    }
  
    def copy(
      `token`: String = this.`token`
    ): stand_args = new Immutable(
      `token`
    )
  
    /**
     * Checks that all required fields are non-null.
     */
    def validate() {
    }
  
    def canEqual(other: Any) = other.isInstanceOf[stand_args]
  
    override def equals(other: Any): Boolean = runtime.ScalaRunTime._equals(this, other)
  
    override def hashCode: Int = runtime.ScalaRunTime._hashCode(this)
  
    override def toString: String = runtime.ScalaRunTime._toString(this)
  
    override def productArity = 1
  
    override def productElement(n: Int): Any = n match {
      case 0 => `token`
      case _ => throw new IndexOutOfBoundsException(n.toString)
    }
  
    override def productPrefix = "stand_args"
  }
  object stand_result extends ThriftStructCodec[stand_result] {
    val Struct = new TStruct("stand_result")
  
    def encode(_item: stand_result, _oproto: TProtocol) { _item.write(_oproto) }
    def decode(_iprot: TProtocol) = Immutable.decode(_iprot)
  
    def apply(_iprot: TProtocol): stand_result = decode(_iprot)
  
    def apply(
    ): stand_result = new Immutable(
    )
  
    def unapply(_item: stand_result): Boolean = true
  
    object Immutable extends ThriftStructCodec[stand_result] {
      def encode(_item: stand_result, _oproto: TProtocol) { _item.write(_oproto) }
      def decode(_iprot: TProtocol) = {
        var _done = false
        _iprot.readStructBegin()
        while (!_done) {
          val _field = _iprot.readFieldBegin()
          if (_field.`type` == TType.STOP) {
            _done = true
          } else {
            _field.id match {
              case _ => TProtocolUtil.skip(_iprot, _field.`type`)
            }
            _iprot.readFieldEnd()
          }
        }
        _iprot.readStructEnd()
        new Immutable(
        )
      }
    }
  
    /**
     * The default read-only implementation of stand_result.  You typically should not need to
     * directly reference this class; instead, use the stand_result.apply method to construct
     * new instances.
     */
    class Immutable(
    ) extends stand_result
  
  }
  
  trait stand_result extends ThriftStruct
    with Product
    with java.io.Serializable
  {
    import stand_result._
  
  
  
    override def write(_oprot: TProtocol) {
      validate()
      _oprot.writeStructBegin(Struct)
      _oprot.writeFieldStop()
      _oprot.writeStructEnd()
    }
  
    def copy(
    ): stand_result = new Immutable(
    )
  
    /**
     * Checks that all required fields are non-null.
     */
    def validate() {
    }
  
    def canEqual(other: Any) = other.isInstanceOf[stand_result]
  
    override def equals(other: Any): Boolean = runtime.ScalaRunTime._equals(this, other)
  
    override def hashCode: Int = runtime.ScalaRunTime._hashCode(this)
  
    override def toString: String = runtime.ScalaRunTime._toString(this)
  
    override def productArity = 0
  
    override def productElement(n: Int): Any = n match {
      case _ => throw new IndexOutOfBoundsException(n.toString)
    }
  
    override def productPrefix = "stand_result"
  }
  object getBalance_args extends ThriftStructCodec[getBalance_args] {
    val Struct = new TStruct("getBalance_args")
    val TokenField = new TField("token", TType.STRING, 1)
  
    def encode(_item: getBalance_args, _oproto: TProtocol) { _item.write(_oproto) }
    def decode(_iprot: TProtocol) = Immutable.decode(_iprot)
  
    def apply(_iprot: TProtocol): getBalance_args = decode(_iprot)
  
    def apply(
      `token`: String
    ): getBalance_args = new Immutable(
      `token`
    )
  
    def unapply(_item: getBalance_args): Option[String] = Some(_item.token)
  
    object Immutable extends ThriftStructCodec[getBalance_args] {
      def encode(_item: getBalance_args, _oproto: TProtocol) { _item.write(_oproto) }
      def decode(_iprot: TProtocol) = {
        var `token`: String = null
        var _got_token = false
        var _done = false
        _iprot.readStructBegin()
        while (!_done) {
          val _field = _iprot.readFieldBegin()
          if (_field.`type` == TType.STOP) {
            _done = true
          } else {
            _field.id match {
              case 1 => { /* token */
                _field.`type` match {
                  case TType.STRING => {
                    `token` = {
                      _iprot.readString()
                    }
                    _got_token = true
                  }
                  case _ => TProtocolUtil.skip(_iprot, _field.`type`)
                }
              }
              case _ => TProtocolUtil.skip(_iprot, _field.`type`)
            }
            _iprot.readFieldEnd()
          }
        }
        _iprot.readStructEnd()
        new Immutable(
          `token`
        )
      }
    }
  
    /**
     * The default read-only implementation of getBalance_args.  You typically should not need to
     * directly reference this class; instead, use the getBalance_args.apply method to construct
     * new instances.
     */
    class Immutable(
      val `token`: String
    ) extends getBalance_args
  
  }
  
  trait getBalance_args extends ThriftStruct
    with Product1[String]
    with java.io.Serializable
  {
    import getBalance_args._
  
    def `token`: String
  
    def _1 = `token`
  
    override def write(_oprot: TProtocol) {
      validate()
      _oprot.writeStructBegin(Struct)
      if (true) {
        val `token_item` = `token`
        _oprot.writeFieldBegin(TokenField)
        _oprot.writeString(`token_item`)
        _oprot.writeFieldEnd()
      }
      _oprot.writeFieldStop()
      _oprot.writeStructEnd()
    }
  
    def copy(
      `token`: String = this.`token`
    ): getBalance_args = new Immutable(
      `token`
    )
  
    /**
     * Checks that all required fields are non-null.
     */
    def validate() {
    }
  
    def canEqual(other: Any) = other.isInstanceOf[getBalance_args]
  
    override def equals(other: Any): Boolean = runtime.ScalaRunTime._equals(this, other)
  
    override def hashCode: Int = runtime.ScalaRunTime._hashCode(this)
  
    override def toString: String = runtime.ScalaRunTime._toString(this)
  
    override def productArity = 1
  
    override def productElement(n: Int): Any = n match {
      case 0 => `token`
      case _ => throw new IndexOutOfBoundsException(n.toString)
    }
  
    override def productPrefix = "getBalance_args"
  }
  object getBalance_result extends ThriftStructCodec[getBalance_result] {
    val Struct = new TStruct("getBalance_result")
    val SuccessField = new TField("success", TType.I32, 0)
  
    def encode(_item: getBalance_result, _oproto: TProtocol) { _item.write(_oproto) }
    def decode(_iprot: TProtocol) = Immutable.decode(_iprot)
  
    def apply(_iprot: TProtocol): getBalance_result = decode(_iprot)
  
    def apply(
      `success`: Option[Int] = None
    ): getBalance_result = new Immutable(
      `success`
    )
  
    def unapply(_item: getBalance_result): Option[Option[Int]] = Some(_item.success)
  
    object Immutable extends ThriftStructCodec[getBalance_result] {
      def encode(_item: getBalance_result, _oproto: TProtocol) { _item.write(_oproto) }
      def decode(_iprot: TProtocol) = {
        var `success`: Int = 0
        var _got_success = false
        var _done = false
        _iprot.readStructBegin()
        while (!_done) {
          val _field = _iprot.readFieldBegin()
          if (_field.`type` == TType.STOP) {
            _done = true
          } else {
            _field.id match {
              case 0 => { /* success */
                _field.`type` match {
                  case TType.I32 => {
                    `success` = {
                      _iprot.readI32()
                    }
                    _got_success = true
                  }
                  case _ => TProtocolUtil.skip(_iprot, _field.`type`)
                }
              }
              case _ => TProtocolUtil.skip(_iprot, _field.`type`)
            }
            _iprot.readFieldEnd()
          }
        }
        _iprot.readStructEnd()
        new Immutable(
          if (_got_success) Some(`success`) else None
        )
      }
    }
  
    /**
     * The default read-only implementation of getBalance_result.  You typically should not need to
     * directly reference this class; instead, use the getBalance_result.apply method to construct
     * new instances.
     */
    class Immutable(
      val `success`: Option[Int] = None
    ) extends getBalance_result
  
  }
  
  trait getBalance_result extends ThriftStruct
    with Product1[Option[Int]]
    with java.io.Serializable
  {
    import getBalance_result._
  
    def `success`: Option[Int]
  
    def _1 = `success`
  
    override def write(_oprot: TProtocol) {
      validate()
      _oprot.writeStructBegin(Struct)
      if (`success`.isDefined) {
        val `success_item` = `success`.get
        _oprot.writeFieldBegin(SuccessField)
        _oprot.writeI32(`success_item`)
        _oprot.writeFieldEnd()
      }
      _oprot.writeFieldStop()
      _oprot.writeStructEnd()
    }
  
    def copy(
      `success`: Option[Int] = this.`success`
    ): getBalance_result = new Immutable(
      `success`
    )
  
    /**
     * Checks that all required fields are non-null.
     */
    def validate() {
    }
  
    def canEqual(other: Any) = other.isInstanceOf[getBalance_result]
  
    override def equals(other: Any): Boolean = runtime.ScalaRunTime._equals(this, other)
  
    override def hashCode: Int = runtime.ScalaRunTime._hashCode(this)
  
    override def toString: String = runtime.ScalaRunTime._toString(this)
  
    override def productArity = 1
  
    override def productElement(n: Int): Any = n match {
      case 0 => `success`
      case _ => throw new IndexOutOfBoundsException(n.toString)
    }
  
    override def productPrefix = "getBalance_result"
  }
  object endSession_args extends ThriftStructCodec[endSession_args] {
    val Struct = new TStruct("endSession_args")
    val TokenField = new TField("token", TType.STRING, 1)
  
    def encode(_item: endSession_args, _oproto: TProtocol) { _item.write(_oproto) }
    def decode(_iprot: TProtocol) = Immutable.decode(_iprot)
  
    def apply(_iprot: TProtocol): endSession_args = decode(_iprot)
  
    def apply(
      `token`: String
    ): endSession_args = new Immutable(
      `token`
    )
  
    def unapply(_item: endSession_args): Option[String] = Some(_item.token)
  
    object Immutable extends ThriftStructCodec[endSession_args] {
      def encode(_item: endSession_args, _oproto: TProtocol) { _item.write(_oproto) }
      def decode(_iprot: TProtocol) = {
        var `token`: String = null
        var _got_token = false
        var _done = false
        _iprot.readStructBegin()
        while (!_done) {
          val _field = _iprot.readFieldBegin()
          if (_field.`type` == TType.STOP) {
            _done = true
          } else {
            _field.id match {
              case 1 => { /* token */
                _field.`type` match {
                  case TType.STRING => {
                    `token` = {
                      _iprot.readString()
                    }
                    _got_token = true
                  }
                  case _ => TProtocolUtil.skip(_iprot, _field.`type`)
                }
              }
              case _ => TProtocolUtil.skip(_iprot, _field.`type`)
            }
            _iprot.readFieldEnd()
          }
        }
        _iprot.readStructEnd()
        new Immutable(
          `token`
        )
      }
    }
  
    /**
     * The default read-only implementation of endSession_args.  You typically should not need to
     * directly reference this class; instead, use the endSession_args.apply method to construct
     * new instances.
     */
    class Immutable(
      val `token`: String
    ) extends endSession_args
  
  }
  
  trait endSession_args extends ThriftStruct
    with Product1[String]
    with java.io.Serializable
  {
    import endSession_args._
  
    def `token`: String
  
    def _1 = `token`
  
    override def write(_oprot: TProtocol) {
      validate()
      _oprot.writeStructBegin(Struct)
      if (true) {
        val `token_item` = `token`
        _oprot.writeFieldBegin(TokenField)
        _oprot.writeString(`token_item`)
        _oprot.writeFieldEnd()
      }
      _oprot.writeFieldStop()
      _oprot.writeStructEnd()
    }
  
    def copy(
      `token`: String = this.`token`
    ): endSession_args = new Immutable(
      `token`
    )
  
    /**
     * Checks that all required fields are non-null.
     */
    def validate() {
    }
  
    def canEqual(other: Any) = other.isInstanceOf[endSession_args]
  
    override def equals(other: Any): Boolean = runtime.ScalaRunTime._equals(this, other)
  
    override def hashCode: Int = runtime.ScalaRunTime._hashCode(this)
  
    override def toString: String = runtime.ScalaRunTime._toString(this)
  
    override def productArity = 1
  
    override def productElement(n: Int): Any = n match {
      case 0 => `token`
      case _ => throw new IndexOutOfBoundsException(n.toString)
    }
  
    override def productPrefix = "endSession_args"
  }
  object endSession_result extends ThriftStructCodec[endSession_result] {
    val Struct = new TStruct("endSession_result")
    val SuccessField = new TField("success", TType.I32, 0)
  
    def encode(_item: endSession_result, _oproto: TProtocol) { _item.write(_oproto) }
    def decode(_iprot: TProtocol) = Immutable.decode(_iprot)
  
    def apply(_iprot: TProtocol): endSession_result = decode(_iprot)
  
    def apply(
      `success`: Option[Int] = None
    ): endSession_result = new Immutable(
      `success`
    )
  
    def unapply(_item: endSession_result): Option[Option[Int]] = Some(_item.success)
  
    object Immutable extends ThriftStructCodec[endSession_result] {
      def encode(_item: endSession_result, _oproto: TProtocol) { _item.write(_oproto) }
      def decode(_iprot: TProtocol) = {
        var `success`: Int = 0
        var _got_success = false
        var _done = false
        _iprot.readStructBegin()
        while (!_done) {
          val _field = _iprot.readFieldBegin()
          if (_field.`type` == TType.STOP) {
            _done = true
          } else {
            _field.id match {
              case 0 => { /* success */
                _field.`type` match {
                  case TType.I32 => {
                    `success` = {
                      _iprot.readI32()
                    }
                    _got_success = true
                  }
                  case _ => TProtocolUtil.skip(_iprot, _field.`type`)
                }
              }
              case _ => TProtocolUtil.skip(_iprot, _field.`type`)
            }
            _iprot.readFieldEnd()
          }
        }
        _iprot.readStructEnd()
        new Immutable(
          if (_got_success) Some(`success`) else None
        )
      }
    }
  
    /**
     * The default read-only implementation of endSession_result.  You typically should not need to
     * directly reference this class; instead, use the endSession_result.apply method to construct
     * new instances.
     */
    class Immutable(
      val `success`: Option[Int] = None
    ) extends endSession_result
  
  }
  
  trait endSession_result extends ThriftStruct
    with Product1[Option[Int]]
    with java.io.Serializable
  {
    import endSession_result._
  
    def `success`: Option[Int]
  
    def _1 = `success`
  
    override def write(_oprot: TProtocol) {
      validate()
      _oprot.writeStructBegin(Struct)
      if (`success`.isDefined) {
        val `success_item` = `success`.get
        _oprot.writeFieldBegin(SuccessField)
        _oprot.writeI32(`success_item`)
        _oprot.writeFieldEnd()
      }
      _oprot.writeFieldStop()
      _oprot.writeStructEnd()
    }
  
    def copy(
      `success`: Option[Int] = this.`success`
    ): endSession_result = new Immutable(
      `success`
    )
  
    /**
     * Checks that all required fields are non-null.
     */
    def validate() {
    }
  
    def canEqual(other: Any) = other.isInstanceOf[endSession_result]
  
    override def equals(other: Any): Boolean = runtime.ScalaRunTime._equals(this, other)
  
    override def hashCode: Int = runtime.ScalaRunTime._hashCode(this)
  
    override def toString: String = runtime.ScalaRunTime._toString(this)
  
    override def productArity = 1
  
    override def productElement(n: Int): Any = n match {
      case 0 => `success`
      case _ => throw new IndexOutOfBoundsException(n.toString)
    }
  
    override def productPrefix = "endSession_result"
  }
  object numPlayersOnline_args extends ThriftStructCodec[numPlayersOnline_args] {
    val Struct = new TStruct("numPlayersOnline_args")
  
    def encode(_item: numPlayersOnline_args, _oproto: TProtocol) { _item.write(_oproto) }
    def decode(_iprot: TProtocol) = Immutable.decode(_iprot)
  
    def apply(_iprot: TProtocol): numPlayersOnline_args = decode(_iprot)
  
    def apply(
    ): numPlayersOnline_args = new Immutable(
    )
  
    def unapply(_item: numPlayersOnline_args): Boolean = true
  
    object Immutable extends ThriftStructCodec[numPlayersOnline_args] {
      def encode(_item: numPlayersOnline_args, _oproto: TProtocol) { _item.write(_oproto) }
      def decode(_iprot: TProtocol) = {
        var _done = false
        _iprot.readStructBegin()
        while (!_done) {
          val _field = _iprot.readFieldBegin()
          if (_field.`type` == TType.STOP) {
            _done = true
          } else {
            _field.id match {
              case _ => TProtocolUtil.skip(_iprot, _field.`type`)
            }
            _iprot.readFieldEnd()
          }
        }
        _iprot.readStructEnd()
        new Immutable(
        )
      }
    }
  
    /**
     * The default read-only implementation of numPlayersOnline_args.  You typically should not need to
     * directly reference this class; instead, use the numPlayersOnline_args.apply method to construct
     * new instances.
     */
    class Immutable(
    ) extends numPlayersOnline_args
  
  }
  
  trait numPlayersOnline_args extends ThriftStruct
    with Product
    with java.io.Serializable
  {
    import numPlayersOnline_args._
  
  
  
    override def write(_oprot: TProtocol) {
      validate()
      _oprot.writeStructBegin(Struct)
      _oprot.writeFieldStop()
      _oprot.writeStructEnd()
    }
  
    def copy(
    ): numPlayersOnline_args = new Immutable(
    )
  
    /**
     * Checks that all required fields are non-null.
     */
    def validate() {
    }
  
    def canEqual(other: Any) = other.isInstanceOf[numPlayersOnline_args]
  
    override def equals(other: Any): Boolean = runtime.ScalaRunTime._equals(this, other)
  
    override def hashCode: Int = runtime.ScalaRunTime._hashCode(this)
  
    override def toString: String = runtime.ScalaRunTime._toString(this)
  
    override def productArity = 0
  
    override def productElement(n: Int): Any = n match {
      case _ => throw new IndexOutOfBoundsException(n.toString)
    }
  
    override def productPrefix = "numPlayersOnline_args"
  }
  object numPlayersOnline_result extends ThriftStructCodec[numPlayersOnline_result] {
    val Struct = new TStruct("numPlayersOnline_result")
    val SuccessField = new TField("success", TType.I32, 0)
  
    def encode(_item: numPlayersOnline_result, _oproto: TProtocol) { _item.write(_oproto) }
    def decode(_iprot: TProtocol) = Immutable.decode(_iprot)
  
    def apply(_iprot: TProtocol): numPlayersOnline_result = decode(_iprot)
  
    def apply(
      `success`: Option[Int] = None
    ): numPlayersOnline_result = new Immutable(
      `success`
    )
  
    def unapply(_item: numPlayersOnline_result): Option[Option[Int]] = Some(_item.success)
  
    object Immutable extends ThriftStructCodec[numPlayersOnline_result] {
      def encode(_item: numPlayersOnline_result, _oproto: TProtocol) { _item.write(_oproto) }
      def decode(_iprot: TProtocol) = {
        var `success`: Int = 0
        var _got_success = false
        var _done = false
        _iprot.readStructBegin()
        while (!_done) {
          val _field = _iprot.readFieldBegin()
          if (_field.`type` == TType.STOP) {
            _done = true
          } else {
            _field.id match {
              case 0 => { /* success */
                _field.`type` match {
                  case TType.I32 => {
                    `success` = {
                      _iprot.readI32()
                    }
                    _got_success = true
                  }
                  case _ => TProtocolUtil.skip(_iprot, _field.`type`)
                }
              }
              case _ => TProtocolUtil.skip(_iprot, _field.`type`)
            }
            _iprot.readFieldEnd()
          }
        }
        _iprot.readStructEnd()
        new Immutable(
          if (_got_success) Some(`success`) else None
        )
      }
    }
  
    /**
     * The default read-only implementation of numPlayersOnline_result.  You typically should not need to
     * directly reference this class; instead, use the numPlayersOnline_result.apply method to construct
     * new instances.
     */
    class Immutable(
      val `success`: Option[Int] = None
    ) extends numPlayersOnline_result
  
  }
  
  trait numPlayersOnline_result extends ThriftStruct
    with Product1[Option[Int]]
    with java.io.Serializable
  {
    import numPlayersOnline_result._
  
    def `success`: Option[Int]
  
    def _1 = `success`
  
    override def write(_oprot: TProtocol) {
      validate()
      _oprot.writeStructBegin(Struct)
      if (`success`.isDefined) {
        val `success_item` = `success`.get
        _oprot.writeFieldBegin(SuccessField)
        _oprot.writeI32(`success_item`)
        _oprot.writeFieldEnd()
      }
      _oprot.writeFieldStop()
      _oprot.writeStructEnd()
    }
  
    def copy(
      `success`: Option[Int] = this.`success`
    ): numPlayersOnline_result = new Immutable(
      `success`
    )
  
    /**
     * Checks that all required fields are non-null.
     */
    def validate() {
    }
  
    def canEqual(other: Any) = other.isInstanceOf[numPlayersOnline_result]
  
    override def equals(other: Any): Boolean = runtime.ScalaRunTime._equals(this, other)
  
    override def hashCode: Int = runtime.ScalaRunTime._hashCode(this)
  
    override def toString: String = runtime.ScalaRunTime._toString(this)
  
    override def productArity = 1
  
    override def productElement(n: Int): Any = n match {
      case 0 => `success`
      case _ => throw new IndexOutOfBoundsException(n.toString)
    }
  
    override def productPrefix = "numPlayersOnline_result"
  }
  class FinagledClient(
    service: FinagleService[ThriftClientRequest, Array[Byte]],
    protocolFactory: TProtocolFactory = new TBinaryProtocol.Factory,
    serviceName: String = "",
    stats: StatsReceiver = NullStatsReceiver
  ) extends FutureIface {
    // ----- boilerplate that should eventually be moved into finagle:
  
    protected def encodeRequest(name: String, args: ThriftStruct) = {
      val buf = new TMemoryBuffer(512)
      val oprot = protocolFactory.getProtocol(buf)
  
      oprot.writeMessageBegin(new TMessage(name, TMessageType.CALL, 0))
      args.write(oprot)
      oprot.writeMessageEnd()
  
      val bytes = Arrays.copyOfRange(buf.getArray, 0, buf.length)
      new ThriftClientRequest(bytes, false)
    }
  
    protected def decodeResponse[T <: ThriftStruct](resBytes: Array[Byte], codec: ThriftStructCodec[T]) = {
      val iprot = protocolFactory.getProtocol(new TMemoryInputTransport(resBytes))
      val msg = iprot.readMessageBegin()
      try {
        if (msg.`type` == TMessageType.EXCEPTION) {
          val exception = TApplicationException.read(iprot) match {
            case sourced: SourcedException =>
              if (serviceName != "") sourced.serviceName = serviceName
              sourced
            case e => e
          }
          throw exception
        } else {
          codec.decode(iprot)
        }
      } finally {
        iprot.readMessageEnd()
      }
    }
  
    protected def missingResult(name: String) = {
      new TApplicationException(
        TApplicationException.MISSING_RESULT,
        "`" + name + "` failed: unknown result"
      )
    }
  
    // ----- end boilerplate.
  
    private[this] val scopedStats = if (serviceName != "") stats.scope(serviceName) else stats
    private[this] object __stats_startSession {
      val RequestsCounter = scopedStats.scope("startSession").counter("requests")
      val SuccessCounter = scopedStats.scope("startSession").counter("success")
      val FailuresCounter = scopedStats.scope("startSession").counter("failures")
      val FailuresScope = scopedStats.scope("startSession").scope("failures")
    }
  
    def startSession(`depositAmount`: Int): Future[String] = {
      __stats_startSession.RequestsCounter.incr()
      this.service(encodeRequest("startSession", startSession_args(depositAmount))) flatMap { response =>
        val result = decodeResponse(response, startSession_result)
        val exception =
          None
        exception.orElse(result.success.map(Future.value)).getOrElse(Future.exception(missingResult("startSession")))
      } rescue {
        case ex: SourcedException => {
          if (this.serviceName != "") { ex.serviceName = this.serviceName }
          Future.exception(ex)
        }
      } onSuccess { _ =>
        __stats_startSession.SuccessCounter.incr()
      } onFailure { ex =>
        __stats_startSession.FailuresCounter.incr()
        __stats_startSession.FailuresScope.counter(ex.getClass.getName).incr()
      }
    }
    private[this] object __stats_beginGame {
      val RequestsCounter = scopedStats.scope("beginGame").counter("requests")
      val SuccessCounter = scopedStats.scope("beginGame").counter("success")
      val FailuresCounter = scopedStats.scope("beginGame").counter("failures")
      val FailuresScope = scopedStats.scope("beginGame").scope("failures")
    }
  
    def beginGame(`token`: String, `betAmount`: Int): Future[Unit] = {
      __stats_beginGame.RequestsCounter.incr()
      this.service(encodeRequest("beginGame", beginGame_args(token, betAmount))) flatMap { response =>
        val result = decodeResponse(response, beginGame_result)
        val exception =
          None
        Future.Done
      } rescue {
        case ex: SourcedException => {
          if (this.serviceName != "") { ex.serviceName = this.serviceName }
          Future.exception(ex)
        }
      } onSuccess { _ =>
        __stats_beginGame.SuccessCounter.incr()
      } onFailure { ex =>
        __stats_beginGame.FailuresCounter.incr()
        __stats_beginGame.FailuresScope.counter(ex.getClass.getName).incr()
      }
    }
    private[this] object __stats_getCurrentBet {
      val RequestsCounter = scopedStats.scope("getCurrentBet").counter("requests")
      val SuccessCounter = scopedStats.scope("getCurrentBet").counter("success")
      val FailuresCounter = scopedStats.scope("getCurrentBet").counter("failures")
      val FailuresScope = scopedStats.scope("getCurrentBet").scope("failures")
    }
  
    def getCurrentBet(`token`: String): Future[Int] = {
      __stats_getCurrentBet.RequestsCounter.incr()
      this.service(encodeRequest("getCurrentBet", getCurrentBet_args(token))) flatMap { response =>
        val result = decodeResponse(response, getCurrentBet_result)
        val exception =
          None
        exception.orElse(result.success.map(Future.value)).getOrElse(Future.exception(missingResult("getCurrentBet")))
      } rescue {
        case ex: SourcedException => {
          if (this.serviceName != "") { ex.serviceName = this.serviceName }
          Future.exception(ex)
        }
      } onSuccess { _ =>
        __stats_getCurrentBet.SuccessCounter.incr()
      } onFailure { ex =>
        __stats_getCurrentBet.FailuresCounter.incr()
        __stats_getCurrentBet.FailuresScope.counter(ex.getClass.getName).incr()
      }
    }
    private[this] object __stats_getGameStatus {
      val RequestsCounter = scopedStats.scope("getGameStatus").counter("requests")
      val SuccessCounter = scopedStats.scope("getGameStatus").counter("success")
      val FailuresCounter = scopedStats.scope("getGameStatus").counter("failures")
      val FailuresScope = scopedStats.scope("getGameStatus").scope("failures")
    }
  
    def getGameStatus(`token`: String): Future[Map[String, String]] = {
      __stats_getGameStatus.RequestsCounter.incr()
      this.service(encodeRequest("getGameStatus", getGameStatus_args(token))) flatMap { response =>
        val result = decodeResponse(response, getGameStatus_result)
        val exception =
          None
        exception.orElse(result.success.map(Future.value)).getOrElse(Future.exception(missingResult("getGameStatus")))
      } rescue {
        case ex: SourcedException => {
          if (this.serviceName != "") { ex.serviceName = this.serviceName }
          Future.exception(ex)
        }
      } onSuccess { _ =>
        __stats_getGameStatus.SuccessCounter.incr()
      } onFailure { ex =>
        __stats_getGameStatus.FailuresCounter.incr()
        __stats_getGameStatus.FailuresScope.counter(ex.getClass.getName).incr()
      }
    }
    private[this] object __stats_hit {
      val RequestsCounter = scopedStats.scope("hit").counter("requests")
      val SuccessCounter = scopedStats.scope("hit").counter("success")
      val FailuresCounter = scopedStats.scope("hit").counter("failures")
      val FailuresScope = scopedStats.scope("hit").scope("failures")
    }
  
    def hit(`token`: String): Future[Unit] = {
      __stats_hit.RequestsCounter.incr()
      this.service(encodeRequest("hit", hit_args(token))) flatMap { response =>
        val result = decodeResponse(response, hit_result)
        val exception =
          None
        Future.Done
      } rescue {
        case ex: SourcedException => {
          if (this.serviceName != "") { ex.serviceName = this.serviceName }
          Future.exception(ex)
        }
      } onSuccess { _ =>
        __stats_hit.SuccessCounter.incr()
      } onFailure { ex =>
        __stats_hit.FailuresCounter.incr()
        __stats_hit.FailuresScope.counter(ex.getClass.getName).incr()
      }
    }
    private[this] object __stats_stand {
      val RequestsCounter = scopedStats.scope("stand").counter("requests")
      val SuccessCounter = scopedStats.scope("stand").counter("success")
      val FailuresCounter = scopedStats.scope("stand").counter("failures")
      val FailuresScope = scopedStats.scope("stand").scope("failures")
    }
  
    def stand(`token`: String): Future[Unit] = {
      __stats_stand.RequestsCounter.incr()
      this.service(encodeRequest("stand", stand_args(token))) flatMap { response =>
        val result = decodeResponse(response, stand_result)
        val exception =
          None
        Future.Done
      } rescue {
        case ex: SourcedException => {
          if (this.serviceName != "") { ex.serviceName = this.serviceName }
          Future.exception(ex)
        }
      } onSuccess { _ =>
        __stats_stand.SuccessCounter.incr()
      } onFailure { ex =>
        __stats_stand.FailuresCounter.incr()
        __stats_stand.FailuresScope.counter(ex.getClass.getName).incr()
      }
    }
    private[this] object __stats_getBalance {
      val RequestsCounter = scopedStats.scope("getBalance").counter("requests")
      val SuccessCounter = scopedStats.scope("getBalance").counter("success")
      val FailuresCounter = scopedStats.scope("getBalance").counter("failures")
      val FailuresScope = scopedStats.scope("getBalance").scope("failures")
    }
  
    def getBalance(`token`: String): Future[Int] = {
      __stats_getBalance.RequestsCounter.incr()
      this.service(encodeRequest("getBalance", getBalance_args(token))) flatMap { response =>
        val result = decodeResponse(response, getBalance_result)
        val exception =
          None
        exception.orElse(result.success.map(Future.value)).getOrElse(Future.exception(missingResult("getBalance")))
      } rescue {
        case ex: SourcedException => {
          if (this.serviceName != "") { ex.serviceName = this.serviceName }
          Future.exception(ex)
        }
      } onSuccess { _ =>
        __stats_getBalance.SuccessCounter.incr()
      } onFailure { ex =>
        __stats_getBalance.FailuresCounter.incr()
        __stats_getBalance.FailuresScope.counter(ex.getClass.getName).incr()
      }
    }
    private[this] object __stats_endSession {
      val RequestsCounter = scopedStats.scope("endSession").counter("requests")
      val SuccessCounter = scopedStats.scope("endSession").counter("success")
      val FailuresCounter = scopedStats.scope("endSession").counter("failures")
      val FailuresScope = scopedStats.scope("endSession").scope("failures")
    }
  
    def endSession(`token`: String): Future[Int] = {
      __stats_endSession.RequestsCounter.incr()
      this.service(encodeRequest("endSession", endSession_args(token))) flatMap { response =>
        val result = decodeResponse(response, endSession_result)
        val exception =
          None
        exception.orElse(result.success.map(Future.value)).getOrElse(Future.exception(missingResult("endSession")))
      } rescue {
        case ex: SourcedException => {
          if (this.serviceName != "") { ex.serviceName = this.serviceName }
          Future.exception(ex)
        }
      } onSuccess { _ =>
        __stats_endSession.SuccessCounter.incr()
      } onFailure { ex =>
        __stats_endSession.FailuresCounter.incr()
        __stats_endSession.FailuresScope.counter(ex.getClass.getName).incr()
      }
    }
    private[this] object __stats_numPlayersOnline {
      val RequestsCounter = scopedStats.scope("numPlayersOnline").counter("requests")
      val SuccessCounter = scopedStats.scope("numPlayersOnline").counter("success")
      val FailuresCounter = scopedStats.scope("numPlayersOnline").counter("failures")
      val FailuresScope = scopedStats.scope("numPlayersOnline").scope("failures")
    }
  
    def numPlayersOnline(): Future[Int] = {
      __stats_numPlayersOnline.RequestsCounter.incr()
      this.service(encodeRequest("numPlayersOnline", numPlayersOnline_args())) flatMap { response =>
        val result = decodeResponse(response, numPlayersOnline_result)
        val exception =
          None
        exception.orElse(result.success.map(Future.value)).getOrElse(Future.exception(missingResult("numPlayersOnline")))
      } rescue {
        case ex: SourcedException => {
          if (this.serviceName != "") { ex.serviceName = this.serviceName }
          Future.exception(ex)
        }
      } onSuccess { _ =>
        __stats_numPlayersOnline.SuccessCounter.incr()
      } onFailure { ex =>
        __stats_numPlayersOnline.FailuresCounter.incr()
        __stats_numPlayersOnline.FailuresScope.counter(ex.getClass.getName).incr()
      }
    }
  }
  class FinagledService(
    iface: FutureIface,
    protocolFactory: TProtocolFactory
  ) extends FinagleService[Array[Byte], Array[Byte]] {
    // ----- boilerplate that should eventually be moved into finagle:
  
    protected val functionMap = new mutable.HashMap[String, (TProtocol, Int) => Future[Array[Byte]]]()
  
    protected def addFunction(name: String, f: (TProtocol, Int) => Future[Array[Byte]]) {
      functionMap(name) = f
    }
  
    protected def exception(name: String, seqid: Int, code: Int, message: String): Future[Array[Byte]] = {
      try {
        val x = new TApplicationException(code, message)
        val memoryBuffer = new TMemoryBuffer(512)
        val oprot = protocolFactory.getProtocol(memoryBuffer)
  
        oprot.writeMessageBegin(new TMessage(name, TMessageType.EXCEPTION, seqid))
        x.write(oprot)
        oprot.writeMessageEnd()
        oprot.getTransport().flush()
        Future.value(Arrays.copyOfRange(memoryBuffer.getArray(), 0, memoryBuffer.length()))
      } catch {
        case e: Exception => Future.exception(e)
      }
    }
  
    protected def reply(name: String, seqid: Int, result: ThriftStruct): Future[Array[Byte]] = {
      try {
        val memoryBuffer = new TMemoryBuffer(512)
        val oprot = protocolFactory.getProtocol(memoryBuffer)
  
        oprot.writeMessageBegin(new TMessage(name, TMessageType.REPLY, seqid))
        result.write(oprot)
        oprot.writeMessageEnd()
  
        Future.value(Arrays.copyOfRange(memoryBuffer.getArray(), 0, memoryBuffer.length()))
      } catch {
        case e: Exception => Future.exception(e)
      }
    }
  
    final def apply(request: Array[Byte]): Future[Array[Byte]] = {
      val inputTransport = new TMemoryInputTransport(request)
      val iprot = protocolFactory.getProtocol(inputTransport)
  
      try {
        val msg = iprot.readMessageBegin()
        functionMap.get(msg.name) map { _.apply(iprot, msg.seqid) } getOrElse {
          TProtocolUtil.skip(iprot, TType.STRUCT)
          exception(msg.name, msg.seqid, TApplicationException.UNKNOWN_METHOD,
            "Invalid method name: '" + msg.name + "'")
        }
      } catch {
        case e: Exception => Future.exception(e)
      }
    }
  
    // ---- end boilerplate.
  
    addFunction("startSession", { (iprot: TProtocol, seqid: Int) =>
      try {
        val args = startSession_args.decode(iprot)
        iprot.readMessageEnd()
        (try {
          iface.startSession(args.depositAmount)
        } catch {
          case e: Exception => Future.exception(e)
        }) flatMap { value: String =>
          reply("startSession", seqid, startSession_result(success = Some(value)))
        } rescue {
          case e => Future.exception(e)
        }
      } catch {
        case e: TProtocolException => {
          iprot.readMessageEnd()
          exception("startSession", seqid, TApplicationException.PROTOCOL_ERROR, e.getMessage)
        }
        case e: Exception => Future.exception(e)
      }
    })
    addFunction("beginGame", { (iprot: TProtocol, seqid: Int) =>
      try {
        val args = beginGame_args.decode(iprot)
        iprot.readMessageEnd()
        (try {
          iface.beginGame(args.token, args.betAmount)
        } catch {
          case e: Exception => Future.exception(e)
        }) flatMap { value: Unit =>
          reply("beginGame", seqid, beginGame_result())
        } rescue {
          case e => Future.exception(e)
        }
      } catch {
        case e: TProtocolException => {
          iprot.readMessageEnd()
          exception("beginGame", seqid, TApplicationException.PROTOCOL_ERROR, e.getMessage)
        }
        case e: Exception => Future.exception(e)
      }
    })
    addFunction("getCurrentBet", { (iprot: TProtocol, seqid: Int) =>
      try {
        val args = getCurrentBet_args.decode(iprot)
        iprot.readMessageEnd()
        (try {
          iface.getCurrentBet(args.token)
        } catch {
          case e: Exception => Future.exception(e)
        }) flatMap { value: Int =>
          reply("getCurrentBet", seqid, getCurrentBet_result(success = Some(value)))
        } rescue {
          case e => Future.exception(e)
        }
      } catch {
        case e: TProtocolException => {
          iprot.readMessageEnd()
          exception("getCurrentBet", seqid, TApplicationException.PROTOCOL_ERROR, e.getMessage)
        }
        case e: Exception => Future.exception(e)
      }
    })
    addFunction("getGameStatus", { (iprot: TProtocol, seqid: Int) =>
      try {
        val args = getGameStatus_args.decode(iprot)
        iprot.readMessageEnd()
        (try {
          iface.getGameStatus(args.token)
        } catch {
          case e: Exception => Future.exception(e)
        }) flatMap { value: Map[String, String] =>
          reply("getGameStatus", seqid, getGameStatus_result(success = Some(value)))
        } rescue {
          case e => Future.exception(e)
        }
      } catch {
        case e: TProtocolException => {
          iprot.readMessageEnd()
          exception("getGameStatus", seqid, TApplicationException.PROTOCOL_ERROR, e.getMessage)
        }
        case e: Exception => Future.exception(e)
      }
    })
    addFunction("hit", { (iprot: TProtocol, seqid: Int) =>
      try {
        val args = hit_args.decode(iprot)
        iprot.readMessageEnd()
        (try {
          iface.hit(args.token)
        } catch {
          case e: Exception => Future.exception(e)
        }) flatMap { value: Unit =>
          reply("hit", seqid, hit_result())
        } rescue {
          case e => Future.exception(e)
        }
      } catch {
        case e: TProtocolException => {
          iprot.readMessageEnd()
          exception("hit", seqid, TApplicationException.PROTOCOL_ERROR, e.getMessage)
        }
        case e: Exception => Future.exception(e)
      }
    })
    addFunction("stand", { (iprot: TProtocol, seqid: Int) =>
      try {
        val args = stand_args.decode(iprot)
        iprot.readMessageEnd()
        (try {
          iface.stand(args.token)
        } catch {
          case e: Exception => Future.exception(e)
        }) flatMap { value: Unit =>
          reply("stand", seqid, stand_result())
        } rescue {
          case e => Future.exception(e)
        }
      } catch {
        case e: TProtocolException => {
          iprot.readMessageEnd()
          exception("stand", seqid, TApplicationException.PROTOCOL_ERROR, e.getMessage)
        }
        case e: Exception => Future.exception(e)
      }
    })
    addFunction("getBalance", { (iprot: TProtocol, seqid: Int) =>
      try {
        val args = getBalance_args.decode(iprot)
        iprot.readMessageEnd()
        (try {
          iface.getBalance(args.token)
        } catch {
          case e: Exception => Future.exception(e)
        }) flatMap { value: Int =>
          reply("getBalance", seqid, getBalance_result(success = Some(value)))
        } rescue {
          case e => Future.exception(e)
        }
      } catch {
        case e: TProtocolException => {
          iprot.readMessageEnd()
          exception("getBalance", seqid, TApplicationException.PROTOCOL_ERROR, e.getMessage)
        }
        case e: Exception => Future.exception(e)
      }
    })
    addFunction("endSession", { (iprot: TProtocol, seqid: Int) =>
      try {
        val args = endSession_args.decode(iprot)
        iprot.readMessageEnd()
        (try {
          iface.endSession(args.token)
        } catch {
          case e: Exception => Future.exception(e)
        }) flatMap { value: Int =>
          reply("endSession", seqid, endSession_result(success = Some(value)))
        } rescue {
          case e => Future.exception(e)
        }
      } catch {
        case e: TProtocolException => {
          iprot.readMessageEnd()
          exception("endSession", seqid, TApplicationException.PROTOCOL_ERROR, e.getMessage)
        }
        case e: Exception => Future.exception(e)
      }
    })
    addFunction("numPlayersOnline", { (iprot: TProtocol, seqid: Int) =>
      try {
        val args = numPlayersOnline_args.decode(iprot)
        iprot.readMessageEnd()
        (try {
          iface.numPlayersOnline()
        } catch {
          case e: Exception => Future.exception(e)
        }) flatMap { value: Int =>
          reply("numPlayersOnline", seqid, numPlayersOnline_result(success = Some(value)))
        } rescue {
          case e => Future.exception(e)
        }
      } catch {
        case e: TProtocolException => {
          iprot.readMessageEnd()
          exception("numPlayersOnline", seqid, TApplicationException.PROTOCOL_ERROR, e.getMessage)
        }
        case e: Exception => Future.exception(e)
      }
    })
  }
  trait ThriftServer extends Service with FutureIface {
    val log = Logger.get(getClass)
  
    def thriftCodec = ThriftServerFramedCodec()
    def statsReceiver: StatsReceiver = new OstrichStatsReceiver
    def tracerFactory: Tracer.Factory = NullTracer.factory
    val thriftProtocolFactory: TProtocolFactory = new TBinaryProtocol.Factory()
    val thriftPort: Int
    val serverName: String
  
    // Must be thread-safe as different threads can start and shutdown the service.
    private[this] val _server = new AtomicReference[Server]
    def server: Server = _server.get
  
    def start() {
      val thriftImpl = new FinagledService(this, thriftProtocolFactory)
      _server.set(serverBuilder.build(thriftImpl))
    }
  
    /**
     * You can override this to provide additional configuration
     * to the ServerBuilder.
     */
    def serverBuilder =
      ServerBuilder()
        .codec(thriftCodec)
        .name(serverName)
        .reportTo(statsReceiver)
        .bindTo(new InetSocketAddress(thriftPort))
        .tracerFactory(tracerFactory)
  
    /**
     * Close the underlying server gracefully with the given grace
     * period. close() will drain the current channels, waiting up to
     * ``timeout'', after which channels are forcibly closed.
     */
    def shutdown(timeout: Duration = 0.seconds) {
      synchronized {
        val s = server
        if (s != null) {
          s.close(timeout)
        }
      }
    }
  }
}