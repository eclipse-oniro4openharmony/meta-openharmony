# SPDX-FileCopyrightText: Huawei Inc.
#
# SPDX-License-Identifier: Apache-2.0

SUMMARY = "A console-only image with OpenHarmony first party components"

LICENSE = "Apache-2.0"

IMAGE_FEATURES += "splash package-management x11-base ssh-server-dropbear"

inherit core-image

# install OpenHarmony components and ptests
IMAGE_INSTALL += "openharmony-standard"

# Let's be friendly enough to provide a fully working interactive shell
IMAGE_INSTALL += "cgroup-lite lxc libdrm-tests"

IMAGE_INSTALL += "packagegroup-core-boot ${CORE_IMAGE_EXTRA_INSTALL}"

IMAGE_ROOTFS_SIZE ?= "8192"
IMAGE_ROOTFS_EXTRA_SPACE:append = " + 8192"

QB_MEM = "-m 1024"

QB_KERNEL_CMDLINE_APPEND += "bootopt=64S3,32N2,64N2 systempart=/dev/mapper/system hardware=x23 ohos.boot.sn=0a20230726rpi fbcon=disable"