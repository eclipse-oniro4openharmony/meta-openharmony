# SPDX-FileCopyrightText: Huawei Inc.
#
# SPDX-License-Identifier: Apache-2.0

SUMMARY = "OpenHarmony standard image"

LICENSE = "Apache-2.0"

IMAGE_FEATURES += "ssh-server-dropbear"

inherit core-image

# install OpenHarmony components
IMAGE_INSTALL += "openharmony-standard"

# install container related components
IMAGE_INSTALL += "cgroup-lite lxc"

IMAGE_INSTALL += "packagegroup-core-boot ${CORE_IMAGE_EXTRA_INSTALL}"

IMAGE_ROOTFS_SIZE ?= "8192"
IMAGE_ROOTFS_EXTRA_SPACE:append = " + 8192"

QB_MEM = "-m 1024"

QB_KERNEL_CMDLINE_APPEND += "bootopt=64S3,32N2,64N2 systempart=/dev/mapper/system hardware=qemu ohos.boot.sn=0a20230726rpi"
