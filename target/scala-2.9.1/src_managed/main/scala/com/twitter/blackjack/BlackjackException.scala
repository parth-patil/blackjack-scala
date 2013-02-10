package com.twitter.blackjack

import com.twitter.scrooge.{ThriftStruct, ThriftStructCodec}
import org.apache.thrift.protocol._
import java.nio.ByteBuffer
import com.twitter.finagle.SourcedException
import scala.collection.mutable
import scala.collection.{Map, Set}

object BlackjackException extends ThriftStructCodec[BlackjackException] {
  val Struct = new TStruct("BlackjackException")
  val DescriptionField = new TField("description", TType.STRING, 1)

  def encode(_item: BlackjackException, _oproto: TProtocol) { _item.write(_oproto) }
  def decode(_iprot: TProtocol) = Immutable.decode(_iprot)

  def apply(_iprot: TProtocol): BlackjackException = decode(_iprot)

  def apply(
    `description`: String
  ): BlackjackException = new Immutable(
    `description`
  )

  def unapply(_item: BlackjackException): Option[String] = Some(_item.description)

  object Immutable extends ThriftStructCodec[BlackjackException] {
    def encode(_item: BlackjackException, _oproto: TProtocol) { _item.write(_oproto) }
    def decode(_iprot: TProtocol) = {
      var `description`: String = null
      var _got_description = false
      var _done = false
      _iprot.readStructBegin()
      while (!_done) {
        val _field = _iprot.readFieldBegin()
        if (_field.`type` == TType.STOP) {
          _done = true
        } else {
          _field.id match {
            case 1 => { /* description */
              _field.`type` match {
                case TType.STRING => {
                  `description` = {
                    _iprot.readString()
                  }
                  _got_description = true
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
        `description`
      )
    }
  }

  /**
   * The default read-only implementation of BlackjackException.  You typically should not need to
   * directly reference this class; instead, use the BlackjackException.apply method to construct
   * new instances.
   */
  class Immutable(
    val `description`: String
  ) extends BlackjackException

}

trait BlackjackException extends SourcedException with ThriftStruct
  with Product1[String]
  with java.io.Serializable
{
  import BlackjackException._

  def `description`: String

  def _1 = `description`

  override def write(_oprot: TProtocol) {
    validate()
    _oprot.writeStructBegin(Struct)
    if (true) {
      val `description_item` = `description`
      _oprot.writeFieldBegin(DescriptionField)
      _oprot.writeString(`description_item`)
      _oprot.writeFieldEnd()
    }
    _oprot.writeFieldStop()
    _oprot.writeStructEnd()
  }

  def copy(
    `description`: String = this.`description`
  ): BlackjackException = new Immutable(
    `description`
  )

  /**
   * Checks that all required fields are non-null.
   */
  def validate() {
  }

  def canEqual(other: Any) = other.isInstanceOf[BlackjackException]

  override def equals(other: Any): Boolean = runtime.ScalaRunTime._equals(this, other)

  override def hashCode: Int = runtime.ScalaRunTime._hashCode(this)

  override def toString: String = runtime.ScalaRunTime._toString(this)

  override def productArity = 1

  override def productElement(n: Int): Any = n match {
    case 0 => `description`
    case _ => throw new IndexOutOfBoundsException(n.toString)
  }

  override def productPrefix = "BlackjackException"
}