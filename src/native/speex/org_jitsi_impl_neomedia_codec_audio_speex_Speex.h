/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class org_jitsi_impl_neomedia_codec_audio_speex_Speex */

#ifndef _Included_org_jitsi_impl_neomedia_codec_audio_speex_Speex
#define _Included_org_jitsi_impl_neomedia_codec_audio_speex_Speex
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     org_jitsi_impl_neomedia_codec_audio_speex_Speex
 * Method:    speex_bits_destroy
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_org_jitsi_impl_neomedia_codec_audio_speex_Speex_speex_1bits_1destroy
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_jitsi_impl_neomedia_codec_audio_speex_Speex
 * Method:    speex_bits_init
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_org_jitsi_impl_neomedia_codec_audio_speex_Speex_speex_1bits_1init
  (JNIEnv *, jclass);

/*
 * Class:     org_jitsi_impl_neomedia_codec_audio_speex_Speex
 * Method:    speex_bits_nbytes
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_org_jitsi_impl_neomedia_codec_audio_speex_Speex_speex_1bits_1nbytes
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_jitsi_impl_neomedia_codec_audio_speex_Speex
 * Method:    speex_bits_read_from
 * Signature: (J[BII)V
 */
JNIEXPORT void JNICALL Java_org_jitsi_impl_neomedia_codec_audio_speex_Speex_speex_1bits_1read_1from
  (JNIEnv *, jclass, jlong, jbyteArray, jint, jint);

/*
 * Class:     org_jitsi_impl_neomedia_codec_audio_speex_Speex
 * Method:    speex_bits_remaining
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_org_jitsi_impl_neomedia_codec_audio_speex_Speex_speex_1bits_1remaining
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_jitsi_impl_neomedia_codec_audio_speex_Speex
 * Method:    speex_bits_reset
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_org_jitsi_impl_neomedia_codec_audio_speex_Speex_speex_1bits_1reset
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_jitsi_impl_neomedia_codec_audio_speex_Speex
 * Method:    speex_bits_write
 * Signature: (J[BII)I
 */
JNIEXPORT jint JNICALL Java_org_jitsi_impl_neomedia_codec_audio_speex_Speex_speex_1bits_1write
  (JNIEnv *, jclass, jlong, jbyteArray, jint, jint);

/*
 * Class:     org_jitsi_impl_neomedia_codec_audio_speex_Speex
 * Method:    speex_decode_int
 * Signature: (JJ[BI)I
 */
JNIEXPORT jint JNICALL Java_org_jitsi_impl_neomedia_codec_audio_speex_Speex_speex_1decode_1int
  (JNIEnv *, jclass, jlong, jlong, jbyteArray, jint);

/*
 * Class:     org_jitsi_impl_neomedia_codec_audio_speex_Speex
 * Method:    speex_decoder_ctl
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_org_jitsi_impl_neomedia_codec_audio_speex_Speex_speex_1decoder_1ctl__JI
  (JNIEnv *, jclass, jlong, jint);

/*
 * Class:     org_jitsi_impl_neomedia_codec_audio_speex_Speex
 * Method:    speex_decoder_ctl
 * Signature: (JII)I
 */
JNIEXPORT jint JNICALL Java_org_jitsi_impl_neomedia_codec_audio_speex_Speex_speex_1decoder_1ctl__JII
  (JNIEnv *, jclass, jlong, jint, jint);

/*
 * Class:     org_jitsi_impl_neomedia_codec_audio_speex_Speex
 * Method:    speex_decoder_destroy
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_org_jitsi_impl_neomedia_codec_audio_speex_Speex_speex_1decoder_1destroy
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_jitsi_impl_neomedia_codec_audio_speex_Speex
 * Method:    speex_decoder_init
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_org_jitsi_impl_neomedia_codec_audio_speex_Speex_speex_1decoder_1init
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_jitsi_impl_neomedia_codec_audio_speex_Speex
 * Method:    speex_encode_int
 * Signature: (J[BIJ)I
 */
JNIEXPORT jint JNICALL Java_org_jitsi_impl_neomedia_codec_audio_speex_Speex_speex_1encode_1int
  (JNIEnv *, jclass, jlong, jbyteArray, jint, jlong);

/*
 * Class:     org_jitsi_impl_neomedia_codec_audio_speex_Speex
 * Method:    speex_encoder_ctl
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_org_jitsi_impl_neomedia_codec_audio_speex_Speex_speex_1encoder_1ctl__JI
  (JNIEnv *, jclass, jlong, jint);

/*
 * Class:     org_jitsi_impl_neomedia_codec_audio_speex_Speex
 * Method:    speex_encoder_ctl
 * Signature: (JII)I
 */
JNIEXPORT jint JNICALL Java_org_jitsi_impl_neomedia_codec_audio_speex_Speex_speex_1encoder_1ctl__JII
  (JNIEnv *, jclass, jlong, jint, jint);

/*
 * Class:     org_jitsi_impl_neomedia_codec_audio_speex_Speex
 * Method:    speex_encoder_destroy
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_org_jitsi_impl_neomedia_codec_audio_speex_Speex_speex_1encoder_1destroy
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_jitsi_impl_neomedia_codec_audio_speex_Speex
 * Method:    speex_encoder_init
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_org_jitsi_impl_neomedia_codec_audio_speex_Speex_speex_1encoder_1init
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_jitsi_impl_neomedia_codec_audio_speex_Speex
 * Method:    speex_lib_get_mode
 * Signature: (I)J
 */
JNIEXPORT jlong JNICALL Java_org_jitsi_impl_neomedia_codec_audio_speex_Speex_speex_1lib_1get_1mode
  (JNIEnv *, jclass, jint);

/*
 * Class:     org_jitsi_impl_neomedia_codec_audio_speex_Speex
 * Method:    speex_resampler_destroy
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_org_jitsi_impl_neomedia_codec_audio_speex_Speex_speex_1resampler_1destroy
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_jitsi_impl_neomedia_codec_audio_speex_Speex
 * Method:    speex_resampler_init
 * Signature: (IIIIJ)J
 */
JNIEXPORT jlong JNICALL Java_org_jitsi_impl_neomedia_codec_audio_speex_Speex_speex_1resampler_1init
  (JNIEnv *, jclass, jint, jint, jint, jint, jlong);

/*
 * Class:     org_jitsi_impl_neomedia_codec_audio_speex_Speex
 * Method:    speex_resampler_process_interleaved_int
 * Signature: (J[BII[BII)I
 */
JNIEXPORT jint JNICALL Java_org_jitsi_impl_neomedia_codec_audio_speex_Speex_speex_1resampler_1process_1interleaved_1int
  (JNIEnv *, jclass, jlong, jbyteArray, jint, jint, jbyteArray, jint, jint);

/*
 * Class:     org_jitsi_impl_neomedia_codec_audio_speex_Speex
 * Method:    speex_resampler_set_rate
 * Signature: (JII)I
 */
JNIEXPORT jint JNICALL Java_org_jitsi_impl_neomedia_codec_audio_speex_Speex_speex_1resampler_1set_1rate
  (JNIEnv *, jclass, jlong, jint, jint);

#ifdef __cplusplus
}
#endif
#endif