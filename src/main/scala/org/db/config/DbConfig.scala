package org.db.config

import ch.rasc.bsoncodec.math.BigDecimalStringCodec
import ch.rasc.bsoncodec.time.LocalDateTimeDateCodec
import com.mongodb.MongoCredential.createCredential
import com.mongodb.{ConnectionString, MongoCredential, ServerAddress}
import org.bson.codecs.configuration.CodecRegistry
import org.db.data.Employee
import org.mongodb.scala.MongoClientSettings

import scala.collection.JavaConverters.seqAsJavaListConverter

object DbConfig {

  val user: String = "root"
  val password: Array[Char] = "example".toCharArray
  val source: String = "admin"
  private val credential: MongoCredential = createCredential(user, source, password)

  import org.bson.codecs.configuration.CodecRegistries
  import org.bson.codecs.configuration.CodecRegistries._
  import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
  import org.mongodb.scala.bson.codecs.Macros._
  import org.mongodb.scala.{MongoClient, MongoCollection, MongoDatabase}


  private val javaCodecs = CodecRegistries.fromCodecs(
    new LocalDateTimeDateCodec(),
    new BigDecimalStringCodec())

  private val registry: CodecRegistry = CodecRegistries.fromProviders(classOf[Employee])
  private val connectionString = s"mongodb://root:example@0.0.0.0:27017/?retryWrites=true&w=majority&maxPoolSize=80&authSource=admin"

//  val settings: MongoClientSettings = MongoClientSettings.builder()
//    .applyToClusterSettings(b => b.hosts(List(new ServerAddress("0.0.0.0:27017")).asJava))
//    .credential(credential)
//    .codecRegistry(fromRegistries(registry, javaCodecs, DEFAULT_CODEC_REGISTRY))
//    .build()

//  val client: MongoClient = MongoClient(settings)
  val client: MongoClient = MongoClient(connectionString)

  val database: MongoDatabase = client.getDatabase("test")
    .withCodecRegistry(fromRegistries(registry, javaCodecs, DEFAULT_CODEC_REGISTRY))

  val employees: MongoCollection[Employee] = database.getCollection("employee")

}
