//
// Created by guoxiaofei on 18-6-5.
//

#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <iostream>
#include <fcntl.h>
#include <string.h>
#include <sys/file.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <android/log.h>
#include "include/com_readsense_app_control_ControlGate.h"
using namespace std;
#define PORT	8000
#pragma pack (1)
struct msg_head{
    uint8_t	sign;		// sign, always 0x7e
    uint8_t dev_sn[16];	//
    uint8_t dev_pa[4];	// passwd
    uint8_t module[4];	// information about myself,(which module send message)
    uint8_t ctl_cate;	// control category
    uint8_t ctl_cmd;	// command
    uint8_t ctl_arg;	// argument
    uint32_t data_len;	// data length, 4 bytes
};
#pragma pack ()

#define LOG_TAG "ControlGate"
#define LOGD(...) ((void)__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__))

template<typename input_iter>
void xor_encrypt(input_iter first, input_iter last, string &key)
{
    size_t key_len=key.length();
    size_t index=0;
    for (input_iter iter=first; iter!=last; iter++,index++){
        if (index >= key_len){
            index = 0;
        }
        *iter ^= key[index];
    }
}
uint8_t checksum(char *start, size_t len)
{
    uint8_t sum=0;
    uint8_t *value=(uint8_t *)start;
    for (size_t i=0; i<len; i++){
        sum += *(value++);
    }
    return sum;
}

int send_data(const char* ip, int flag)
{
    uint8_t door;//0 is open, 1 is close
    if (1==flag){
        door = 0;
        LOGD("door open\n");
    }else if(0==flag){
        door=1;
        LOGD("door close\n");
    }else{
        LOGD("invalid argument, only 0 and 1 is supported\n");
        return -1;
    }

    // they said the data must less than 340 byte, so 512(>340 + sizeof (msg_head)) is enough
    char *buff=(char *)calloc(512 ,1);
    if (NULL == buff){
        perror("calloc error");
        return -1;
    }
    msg_head *head=(msg_head *)buff;

    int client_sock;
    client_sock = socket(AF_INET, SOCK_STREAM, 0);
    if (-1 == client_sock) {
        perror("socket");
        return -1;
    }
    struct sockaddr_in local_addr;
    memset(&local_addr, 0, sizeof(local_addr));
    local_addr.sin_family = AF_INET;
    local_addr.sin_port = htons(PORT);
    inet_aton(ip, &local_addr.sin_addr);

    if (-1 == connect(client_sock, (struct sockaddr*)&local_addr, sizeof (local_addr))){
        perror("connect");
        return -1;
    }


    head->sign = 0x7e;
    head->dev_pa[0] = 0xff;
    head->dev_pa[1] = 0xff;
    head->dev_pa[2] = 0xff;
    head->dev_pa[3] = 0xff;
    head->module[0] = 0x1a;
    head->module[1] = 0xf6;
    head->module[2] = 0x42;
    head->module[3] = 0x29;

    memcpy(head->dev_sn, "BK-9910T17110524", 16);
    head->ctl_cate = 0x03;
    //head->ctl_cmd = 0x02;

    head->ctl_cmd = 0x03;
    head->ctl_arg = door;
    head->data_len =htonl(0x04);

    uint8_t *data = (uint8_t *)(head + 1);
    *data = 0x01;	//door 1
    *(data+1) = 0x00;	//door 2
    *(data+2) = 0x00;	//door 3
    *(data+3) = 0x00;	//door 4

    size_t total_len = sizeof (msg_head) + ntohl(head->data_len);
    uint8_t *sum = data + ntohl(head->data_len);
    *sum = checksum(buff+1, total_len-1);
    uint8_t *sign_end = sum + 1;
    *sign_end = 0x7e;
    total_len += 2;

    LOGD("tag");
    //scanf("%ms", &str);
    send(client_sock, buff, total_len, 0);

    uint8_t recv_buff[512]={0};


    ssize_t recv_len = recv(client_sock, &recv_buff, sizeof (recv_buff), 0);
    printf("have received %d bytes data\n", recv_len);

    free(buff);
    close(client_sock);
    return 0;
}


JNIEXPORT jint JNICALL Java_com_readsense_app_control_ControlGate_sendCmd
        (JNIEnv *env, jclass clazz, jstring ip, jint flag) {
    jboolean isCopy;
    const char * ip_str = env->GetStringUTFChars(ip, &isCopy);
    if (ip_str == NULL) {
        LOGD("GetStringUTFChars error");
        return -1;
    }
    int result =  send_data(ip_str, flag);
    env->ReleaseStringUTFChars(ip, ip_str);
    return result;
}