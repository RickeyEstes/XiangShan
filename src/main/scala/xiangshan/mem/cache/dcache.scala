package xiangshan.mem.cache

import chisel3._
import chisel3.util._
import xiangshan._
import xiangshan.utils._
import chisel3.util.experimental.BoringUtils
import xiangshan.backend.decode.XSTrap
import xiangshan.mem._
import xiangshan.mem.pipeline._
import bus.simplebus._

object CacheOp {
  def load   = "b00".U
  def store  = "b01".U
  def refill = "b11".U
  def empty  = "b10".U

  def width  = load.getWidth
}

class DcacheUserBundle extends XSBundle with HasMEMConst {
  val uop = Output(new MicroOp) //FIXME: opt data width
}

class DCacheLoadReq extends XSBundle
{
  val paddr  = UInt(PAddrBits.W)
  val vaddr  = UInt(PAddrBits.W)
  val data  = Bits(CacheLineSize.W)
}

class DCacheStoreReq extends XSBundle
{
  val paddr  = UInt(PAddrBits.W)
  val data  = Bits(CacheLineSize.W)
  val mask  = Bits((CacheLineSize/8).W)
}

class DCacheResp extends XSBundle {
  val data = Bits(XLEN.W)
}

class DCacheLoadIO extends XSBundle
{
  val req = Flipped(DecoupledIO(new DCacheLoadReq))
  val resp = DecoupledIO(new DCacheResp)
}

class DCacheStoreIO extends XSBundle
{
  val req = Flipped(DecoupledIO(new DCacheStoreReq))
  val resp = DecoupledIO(new DCacheResp)
}

class DCacheIO extends XSBundle with HasMEMConst {
  val load = Vec(LoadPipelineWidth, new DCacheLoadIO)
  val store = new DCacheStoreIO
}

class Dcache extends XSModule with NeedImpl{
  val io = IO(new DCacheIO)
  
  // Arbitor for 2 dcache ports in built in decache
  // store/refill only use port0, port1 is always assigned to load request

  // priority:
  // load
  // store
  // refill
}