package com.tecpie.platform.s3.config;

import com.amazonaws.regions.Regions;
import com.tecpie.platform.s3.exception.UnknownRegionException;

public class AWSS3RegionConfig {

    public static Regions getRegions(String region) {
        switch (region) {
            case "us-gov-west-1":
                return Regions.GovCloud;
            case "us-gov-east-1":
                return Regions.US_GOV_EAST_1;
            case "us-east-1":
                return Regions.US_EAST_1;
            case "us-east-2":
                return Regions.US_EAST_2;
            case "us-west-1":
                return Regions.US_WEST_1;
            case "us-west-2":
                return Regions.US_WEST_2;
            case "eu-west-1":
                return Regions.EU_WEST_1;
            case "eu-west-2":
                return Regions.EU_WEST_2;
            case "eu-west-3":
                return Regions.EU_WEST_3;
            case "eu-central-1":
                return Regions.EU_CENTRAL_1;
            case "eu-north-1":
                return Regions.EU_NORTH_1;
            case "ap-east-1":
                return Regions.AP_EAST_1;
            case "ap-south-1":
                return Regions.AP_SOUTH_1;
            case "ap-southeast-1":
                return Regions.AP_SOUTHEAST_1;
            case "ap-southeast-2":
                return Regions.AP_SOUTHEAST_2;
            case "ap-northeast-1":
                return Regions.AP_NORTHEAST_1;
            case "ap-northeast-2":
                return Regions.AP_NORTHEAST_2;
            case "sa-east-1":
                return Regions.SA_EAST_1;
            case "cn-north-1":
                return Regions.CN_NORTH_1;
            case "cn-northwest-1":
                return Regions.CN_NORTHWEST_1;
            case "ca-central-1":
                return Regions.CA_CENTRAL_1;
            case "me-south-1":
                return Regions.ME_SOUTH_1;
            default:
                throw new UnknownRegionException("配置区域未知, 请选择正确的区域");
        }
    }
}
