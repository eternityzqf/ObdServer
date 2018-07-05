package com.uoocent.car.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.uoocent.car.config.Config;
import com.uoocent.car.entity.Car;
import com.uoocent.car.entity.CarBrand;
import com.uoocent.car.entity.Device;
import com.uoocent.car.service.CarService;
import com.uoocent.car.service.DeviceService;
import com.uoocent.car.util.ResponseCode;
import com.uoocent.car.util.Result;
import com.uoocent.car.util.GPSToBaiduGPS;
import com.uoocent.car.util.Constant;
import com.uoocent.car.util.DateUtils;
import com.uoocent.car.util.FileUtils;

@Api(value = "obd", description = "设备")
@Controller
public class ObdController {
	private static final Logger logger = Logger.getLogger(ObdController.class);
	@Resource
	private DeviceService deviceService;
	@Resource
	private CarService carService;
	@Resource
	private Config config;
	@Resource
	private FileUtils fileUtils;

	@ApiOperation(value = "获取sim卡号", httpMethod = "GET", response = Result.class, notes = "登录必须的参数：obdcode,isCover 是否覆盖设备")
	@RequestMapping(value = "app/obd/getsimcode", method = RequestMethod.GET)
	@ResponseBody
	public Result getSimCode(String obdcode,String device_type,String ex, HttpServletRequest request) {
		try {
			Result res = Result.error(ResponseCode.missing_parameter.getMsg());
			if (StringUtils.isBlank(obdcode) || StringUtils.isBlank(device_type)) {
				return res;
			}
			if(Constant.DEVICE_TYPE_A9.equals(device_type)){
				if(!a9DeviceCheck(obdcode)){
					return Result.instance(ResponseCode.error.getCode(), "A9设备编号不能包含字母、空格或特殊符号，请重新输入！");
				}
			} else if (Constant.DEVICE_TYPE_OBD.equals(device_type)){
				if(!obdDeviceCheck(obdcode)){
					return Result.instance(ResponseCode.error.getCode(), "OBD设备编号不能包含空格或特殊符号，请重新输入！");
				}
			} else if(Constant.DEVICE_TYPE_KM01.equals(device_type)){
				if(!km01DeviceCheck(obdcode)){
					return Result.instance(ResponseCode.error.getCode(), "KM01设备编号不能包含字母、空格或特殊符号，请重新输入！");
				}
			} else if(Constant.DEVICE_TYPE_VIDEO.equals(device_type)){
				if(!videoDeviceCheck(obdcode)){
					return Result.instance(ResponseCode.error.getCode(), "视频设备编号不能包含字母、空格或特殊符号，请重新输入！");
				}
			}
			String extend_fuel = request.getParameter("extend_fuel");
			String extend_trun = request.getParameter("extend_trun");
			String extend_water = request.getParameter("extend_water");
			if(Constant.EXTEND_DEVICE.equals(extend_fuel) || Constant.EXTEND_DEVICE.equals(extend_water)){
				extend_fuel = Constant.EXTEND_DEVICE;
			}else{
				extend_fuel = Constant.UNEXTEND_DEVICE;
			}
			if(Constant.EXTEND_DEVICE.equals(extend_trun)){
			    extend_trun = Constant.EXTEND_DEVICE;
            }else{
			    extend_trun = Constant.UNEXTEND_DEVICE;
            }
            Result check = checkDeviceAndCarNo(obdcode,"",ex,"0");
			if(check.getCode() != 10000){
			    return check;
            }
			Map<String,String> map = new HashMap<String,String>();
			map.put("device", obdcode);
			map.put("ip", config.getIp());
			map.put("port", config.getPort());
			map.put("exchange", config.getExchange());
			map.put("extend_fuel", extend_fuel);
			map.put("extend_trun", extend_trun);
			Device device = new Device();
			if(Constant.DEVICE_TYPE_OBD.equals(device_type)){
				device = deviceService.findByODevice(obdcode);
				if(device == null){
					return Result.error("没有获取到该设备绑定的sim卡号，请确认设备是否插入sim卡，并开机");
				}
			}else if (Constant.DEVICE_TYPE_A9.equals(device_type) || Constant.DEVICE_TYPE_KM01.equals(device_type)  || Constant.DEVICE_TYPE_VIDEO.equals(device_type)){
				device.setDevice(obdcode);
				device.setSim("0000000000000");
				device.setVin("0000000000000");
			}
            deviceService.updateDispatcher(map,true);
			return Result.success(device);
		} catch (Exception e) {
			logger.error("app/obd/getsimcode", e);
			return Result.error(e.getMessage());
		}
	}
	
	@ApiOperation(value = "设备保存", httpMethod = "POST", response = Result.class, notes = "必须的参数：{vehicnum:车牌号,obdnum:终端编号,simnum:SIM号,odometer:里程表读数,connectway:接线方式,org:机构id}")
 	@RequestMapping(value="app/obd/upload", method = RequestMethod.POST)
 	@ResponseBody
 	public Result obdInfoUpload(Boolean isCoverCarNo,Boolean isCoverDevice,HttpServletRequest request) {
		try {
			String uid = request.getParameter("uid");
			String vehicnum = request.getParameter("vehicnum");
			String car_no_color = request.getParameter("car_no_color");
			String self_num = request.getParameter("selfnum");
			String car_brand = request.getParameter("car_brand");
			String type = request.getParameter("type");
			String fule_type = request.getParameter("fule_type");
			String driver_image_1 = request.getParameter("driver_image_1");
			String driver_image_2 = request.getParameter("driver_image_2");
			String tmp_tv = request.getParameter("tank_vol");
			String tmp_tl = request.getParameter("tank_length");
			String tmp_th = request.getParameter("tank_height");
			String tmp_tw = request.getParameter("tank_width");
			Double tank_vol = null,tank_length = null,tank_height = null,tank_width = null;
			if(StringUtils.isNotBlank(tmp_tv)){
				tank_vol = Double.valueOf(tmp_tv);
			}
			if(StringUtils.isNotBlank(tmp_tl)){
				tank_length = Double.valueOf(tmp_tl);
			}
			if(StringUtils.isNotBlank(tmp_th)){
				tank_height = Double.valueOf(tmp_th);
			}
			if(StringUtils.isNotBlank(tmp_tw)){
				tank_width = Double.valueOf(tmp_tw);
			}
			String obdnum = request.getParameter("obdnum");
			String simnum = request.getParameter("simnum");
			String odometer = request.getParameter("odometer");
			String connectway = request.getParameter("connectway");
			String remark = request.getParameter("remark");
			String org = request.getParameter("org");
			String vin = request.getParameter("vin");
			String device_type = request.getParameter("device_type");
			String video_device = request.getParameter("video_device");
			String video_sim = request.getParameter("video_sim");
			String extend_fuel = request.getParameter("extend_fuel");
			String extend_trun = request.getParameter("extend_trun");
			String extend_water = request.getParameter("extend_water");
            String cameraCount = request.getParameter("cameracount");//摄像头个数
            cameraCount = cameraCount == null || cameraCount.trim().equals("")? "0" : cameraCount;
            String screenCount = request.getParameter("screencount");//显示屏个数
            screenCount = screenCount == null || screenCount.trim().equals("")? "0" : screenCount;
            String interphoneCount = request.getParameter("interphonecount");//对讲机套数
            interphoneCount = interphoneCount == null || interphoneCount.trim().equals("")? "0" : interphoneCount;
            String warnerCount = request.getParameter("warnercount");//报警器套数
            warnerCount = warnerCount == null || warnerCount.trim().equals("")? "0" : warnerCount;
            String extend = extend_fuel + "," + extend_water + "," + extend_trun;
			//参数验证
			Result res = Result.error(ResponseCode.missing_parameter.getMsg());
			if(StringUtils.isBlank(uid) 
			    || (StringUtils.isBlank(vehicnum) && StringUtils.isBlank(self_num)) 
				|| StringUtils.isBlank(type) 
				|| StringUtils.isBlank(car_brand)
				|| StringUtils.isBlank(obdnum) 
				|| StringUtils.isBlank(simnum) 
				|| StringUtils.isBlank(connectway) 
				|| StringUtils.isBlank(org)
				|| StringUtils.isBlank(vin)
				|| StringUtils.isBlank(device_type)
			){
				return res;
			}
			if(Constant.DEVICE_TYPE_A9.equals(device_type)){
				if(!a9DeviceCheck(obdnum)){
					return Result.instance(ResponseCode.error.getCode(), "A9设备编号不能包含字母、空格或特殊符号，请重新输入！");
				}
			} else if (Constant.DEVICE_TYPE_OBD.equals(device_type)){
				if(!obdDeviceCheck(obdnum)){
					return Result.instance(ResponseCode.error.getCode(), "OBD设备编号不能包含空格或特殊符号，请重新输入！");
				}
			} else if(Constant.DEVICE_TYPE_KM01.equals(device_type)){
				if(!km01DeviceCheck(obdnum)){
					return Result.instance(ResponseCode.error.getCode(), "KM01设备编号不能包含字母、空格或特殊符号，请重新输入！");
				}
			} else if(Constant.DEVICE_TYPE_VIDEO.equals(device_type)){
				if(!videoDeviceCheck(video_device)){
					return Result.instance(ResponseCode.error.getCode(), "视频设备编号不能包含字母、空格或特殊符号，请重新输入！");
				}
			}
			
			if(!simCodeCheck(simnum)){
				return Result.instance(ResponseCode.error.getCode(), "sim卡号不能输入空格或特殊符号，请重新输入！");
			}
			if(StringUtils.isNotBlank(video_device) && !videoDeviceCheck(video_device)){
				return Result.instance(ResponseCode.error.getCode(), "附加设备编号不能包含字母、空格或特殊符号，请重新输入！");
			}
			if(StringUtils.isNotBlank(video_sim) && !simCodeCheck(video_sim)){
				return Result.instance(ResponseCode.error.getCode(), "附加设备sim卡号不能包含空格或特殊符号，请重新输入！");
			}
			if(StringUtils.isBlank(vehicnum)){//当车牌号为空时，车牌号默认填充为自编号
				vehicnum = self_num;
			}
			Result check = checkDeviceAndCarNo(obdnum,vehicnum,"","1");
			if(check.getCode() != 10000){
			    return check;
            }
			
 			//设置车辆信息
			Car cc = new Car();
 			cc.setCar_no(vehicnum);
 			cc.setCar_no_color(car_no_color);
			cc.setSelf_num(self_num);
			cc.setBrand(car_brand);
			cc.setType(type);
			cc.setFule_type(fule_type);
			cc.setTank_vol(tank_vol);
			cc.setTank_length(tank_length);
			cc.setTank_height(tank_height);
			cc.setTank_width(tank_width);
			driver_image_1 = uploadFile(driver_image_1);
			cc.setDriver_image_1(driver_image_1);
			driver_image_2 = uploadFile(driver_image_2);
			cc.setDriver_image_2(driver_image_2);
 			cc.setVin(vin);
			if(StringUtils.isNotBlank(remark)){
				cc.setTexts(remark);
			}
			if(StringUtils.isBlank(odometer)){
				long k = 0;
				cc.setMileage(k);
			}else{
				cc.setMileage(Long.parseLong(odometer));
			}
 			cc.setObd(Constant.BOUND_OBD);
 			cc.setStatus(Constant.ADD_STATUS);
 			cc.setCar_status(Constant.UN_CHECK_STATUS);
 			cc.setOrg(Long.parseLong(org));
 			cc.setCreatedby(Long.parseLong(uid));
 			cc.setCreatetime(DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
			
 			//设置设备信息
			Device dd= new Device();
			dd.setCar_no(vehicnum);
			dd.setDevice(obdnum);
			dd.setDevice_type(device_type);
			if(Constant.DEVICE_TYPE_VIDEO.equals(device_type)){
				dd.setVideo_device(simnum);
				dd.setVideo_sim(video_sim);
			} else {
				dd.setVideo_device(video_device);
				dd.setVideo_sim(video_sim);
			}
			dd.setSim(simnum);
			dd.setConnectway(connectway);
			dd.setCreatedate(DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
			dd.setStatus(Constant.ADD_STATUS);
 			dd.setCreatedby(Long.parseLong(uid));
 			dd.setExtend(extend);
            dd.setCameraCount(Integer.parseInt(cameraCount));
            dd.setScreenCount(Integer.parseInt(screenCount));
            dd.setInterphoneCount(Integer.parseInt(interphoneCount));
            dd.setWarnerCount(Integer.parseInt(warnerCount));

 			//新增车辆信息和设备信息
 			deviceService.addDeviceAndCar(dd, cc);


            HashMap<String, Object> maps = new HashMap<String, Object>();
            maps.put("uid", uid);
            maps.put("vehicnum", vehicnum);
            maps.put("car_no_color",car_no_color);
            maps.put("selfnum",self_num);
            maps.put("car_brand", car_brand);
            maps.put("type",type);
            maps.put("fule_type", fule_type);
            maps.put("tank_vol", tank_vol);
            maps.put("tank_length", tank_length);
            maps.put("tank_height", tank_height);
            maps.put("tank_width", tank_width);
            maps.put("driver_image_1", driver_image_1);
            maps.put("driver_image_2", driver_image_2);
            maps.put("obdnum", obdnum);
            maps.put("device_type", device_type);
            maps.put("video_device", video_device);
            maps.put("video_sim", video_sim);
            maps.put("simnum", simnum);
            maps.put("odometer", odometer);
            maps.put("connectway", connectway);
            maps.put("remark", remark);
            maps.put("org", org);
            maps.put("vin", vin);
            maps.put("extend_fuel", extend_fuel);
            maps.put("extend_water", extend_water);
            maps.put("extend_trun", extend_trun);
            maps.put("cameracount",cameraCount);
            maps.put("screencount",screenCount);
            maps.put("interphonecount",interphoneCount);
            maps.put("warnercount",warnerCount);
 			return Result.success(maps);
 		} catch (Exception e) {
 			logger.error("app/obd/upload", e);
 			return Result.error(e.getMessage());
 		}
 	}
	
	@ApiOperation(value = "获取车辆列表", httpMethod = "POST", response = Result.class, notes = "必须的参数：{uid:登录用户id,page:页码,size:每页显示条数}")
 	@RequestMapping(value="app/obd/getvehiclelist", method = RequestMethod.POST)
 	@ResponseBody
 	public Result getVehicleList(String uid ,Integer page, Integer size, HttpServletRequest request){
		try {
			//参数验证
			Result res = Result.error(ResponseCode.missing_parameter.getMsg());
			if(StringUtils.isBlank(uid) || page == null || size == null){
				return res;
			}
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("obd", Constant.BOUND_OBD);
			map.put("status", Constant.ADD_STATUS);
			map.put("createdby", uid);
			map.put("startIdx", page*size);
			map.put("limit", size);
			JSONObject jsonCarList = carService.listVehiclePage(map);
			return Result.success(jsonCarList);
		} catch (Exception e){
 			logger.error("app/obd/getvehiclelist", e);
 			return Result.error(e.getMessage());
		}
	}
	
	@ApiOperation(value = "获取obd数据和报警数据", httpMethod = "GET", response = Result.class, notes = "必须的参数：{obdcode:设备编号}")
 	@RequestMapping(value="app/obd/getdata", method = RequestMethod.GET)
 	@ResponseBody
	public Result getObdData(String obdcode, HttpServletRequest request){
		try {
			//参数验证
			Result res = Result.error(ResponseCode.missing_parameter.getMsg());
			if(StringUtils.isBlank(obdcode)){
				return res;
			}
			String exchange = config.getExchange();
			JSONObject jsonData = deviceService.getObdData(obdcode,exchange);
			int cTotal = jsonData.getJSONArray("car_notice").size();
			int gTotal = jsonData.getJSONArray("gps").size();
			if(cTotal == 0 && gTotal == 0){
				return Result.error("获取不到该数据状态，确保该设备能正常获取sim卡信息");
			}
			return Result.success(jsonData);
		} catch (Exception e) {
			logger.error("app/obd/getdata", e);
			return Result.error(e.getMessage());
		}
	}
	
	@ApiOperation(value = "获取车辆品牌", httpMethod = "GET", response = Car.class)
	@RequestMapping(value="app/obd/getcarbrand", method = RequestMethod.GET)
	@ResponseBody
	public Result getCarBrand() {
		try {
			List<CarBrand> list = new ArrayList<CarBrand>();
			JSONArray parseArray = JSON.parseArray(Constant.CAR_BRAND_JSON);
			for (int i = 0; i < parseArray.size(); i++) {
				JSONObject jb = parseArray.getJSONObject(i);
				String name = jb.getString("text");
				list.add(new CarBrand(name, name));
			}
			return Result.success(list);
		} catch (Exception e) {
			logger.error("app/obd/getcarbrand", e);
			return Result.error(e.getMessage());
		}
	}
	
	private String uploadFile(String url){
		if(StringUtils.isBlank(url)){
			return null;
		}
		if(url.startsWith("tmp")){
			return fileUtils.uploadFile(url);
		}else{
			return url;
		}
	}
	
	@ApiOperation(value = "获取车辆信息", httpMethod = "POST", response = Result.class, notes = "登录必须的参数：vehicnum 车牌号")
	@RequestMapping(value = "app/obd/getcarinfo", method = RequestMethod.POST)
	@ResponseBody
	public Result getCarInfo(String vehicnum, HttpServletRequest request) {
		try {
			Result res = Result.error(ResponseCode.missing_parameter.getMsg());
			if (StringUtils.isBlank(vehicnum)) {
				return res;
			}
			Map<String,String> map = new HashMap<String,String>();
			map.put("car_no", vehicnum);
			map.put("car_status", Constant.ADD_STATUS);
			map.put("device_status", Constant.ADD_STATUS);
			Map<String,String> carInfo = carService.getCarInfo(map);
			return Result.success(carInfo);
		} catch (Exception e) {
			logger.error("app/obd/getcarinfo", e);
			return Result.error(e.getMessage());
		}
	}
	
	@ApiOperation(value = "获取设备位置信息", httpMethod = "GET", response = Result.class, notes = "登录必须的参数：obdcode 设备编号")
	@RequestMapping(value = "app/obd/getcarlocation", method = RequestMethod.GET)
	@ResponseBody
	public Result getCarLocation(String obdcode, HttpServletRequest request) {

		try {
			Result res = Result.error(ResponseCode.missing_parameter.getMsg());
			if (StringUtils.isBlank(obdcode)) {
				return res;
			}
			Map<String,String> map = new HashMap<String,String>();
			map.put("device", obdcode);
			map.put("car_status", Constant.ADD_STATUS);
			map.put("device_status", Constant.ADD_STATUS);
			Map<String,Object> carLocation = carService.getCarLocation(map);
			
			if(carLocation != null && carLocation.get("latitude") != null && carLocation.get("longitude") != null){
				double[] tmpGPS = GPSToBaiduGPS.wgs2bd(Double.parseDouble(carLocation.get("latitude").toString()), Double.parseDouble(carLocation.get("longitude").toString()));
				carLocation.put("latitude", tmpGPS[0]);
				carLocation.put("longitude", tmpGPS[1]);
			}
			return Result.success(carLocation);
		} catch (Exception e) {
			logger.error("app/obd/getcarlocation", e);
			return Result.error(e.getMessage());
		}
	}
	
	@ApiOperation(value = "获取设备gps、报警、油感/液位高度、正反转数据", httpMethod = "GET", response = Result.class, notes = "必须的参数：{obdcode:设备编号}")
 	@RequestMapping(value="app/obd/getdevicedata", method = RequestMethod.GET)
 	@ResponseBody
	public Result getDeviceData(String obdcode, HttpServletRequest request){
		try {
			//参数验证
			Result res = Result.error(ResponseCode.missing_parameter.getMsg());
			if(StringUtils.isBlank(obdcode)){
				return res;
			}
			JSONObject jsonData = deviceService.getDeviceInfoData(obdcode);
			int gTotal = jsonData.getJSONArray("gps").size();
			int cTotal = jsonData.getJSONArray("car_notice").size();
			int vTotal = jsonData.getJSONArray("volheight_trun").size();
			if(cTotal == 0 && gTotal == 0 && vTotal == 0){
				return Result.error("获取不到该设备数据状态，确保该设备正常运行");
			}
			return Result.success(jsonData);
		} catch (Exception e) {
			logger.error("app/obd/getdevicedata", e);
			return Result.error(e.getMessage());
		}
	}

	@ApiOperation(value = "解除设备绑定", httpMethod = "GET", response = Result.class, notes = "必须的参数：{obdCode:设备编号}")
	@RequestMapping(value="app/obd/unbundling", method = RequestMethod.GET)
	@ResponseBody
	public Result unBundling(String obdCode){
		HashMap<String, String> result = new HashMap<String, String>();
		try {
			//参数验证
			Result res = Result.error(ResponseCode.missing_parameter.getMsg());
			if(StringUtils.isBlank(obdCode)){
				return res;
			}
			HashMap<String, Object> query = new HashMap<String, Object>();
			query.put("device",obdCode);
			query.put("status",Constant.ADD_STATUS);
			Device device = deviceService.findByMap(query);
			Long carId = 0L;
			if(null != device && device.getCar_id() > 0){
			    carId = device.getCar_id();
            }
			deviceService.removeCarAndDevice(obdCode,carId);
			result.put("status","1");
			result.put("msg","解绑成功");
			return Result.success(result);
		} catch (Exception e) {
			logger.error("app/obd/unbundling", e);
			return Result.error(e.getMessage());
		}
	}

    @ApiOperation(value = "检查设备或车辆是否已绑定", httpMethod = "GET", response = Result.class, notes = "必须的参数：{obdCode:设备编号,carNo:车牌号}")
    @RequestMapping(value="app/obd/checkDeviceAndCarNo", method = RequestMethod.GET)
    @ResponseBody
    public Result checkDeviceAndCarNo(String obdCode,String carNo, String ex, String isSave){
        try {
        	if(null == isSave) isSave = "0";
            //参数验证
            Result res = Result.error(ResponseCode.missing_parameter.getMsg());
            if(StringUtils.isBlank(carNo) && StringUtils.isBlank(obdCode)){
                return res;
            }
            if(StringUtils.isNotBlank(carNo)){
                HashMap<String, Object> query = new HashMap<String, Object>();
                query.put("car_no",carNo);
                query.put("status",Constant.ADD_STATUS);
                Car car = carService.findByMap(query);
                if(null != car && car.getId() > 0){
                    return new Result(20000,"该车辆已绑定设备，请先解绑！",car);
                }
            }
            if(StringUtils.isNotBlank(obdCode)){
                HashMap<String, Object> query = new HashMap<String, Object>();
                query.put("device",obdCode);
                query.put("status",Constant.ADD_STATUS);
                Device device = deviceService.findByMap(query);
                if(null != device && device.getId() > 0){
                    return new Result(20000,"该设备已绑定车辆，请先解绑！",device);
                }else if(isSave.equals("0")){
                    Map<String, String> deviceO = deviceService.checkDeviceO(obdCode);
                    if(null != deviceO && null != deviceO.get("exchange")){
                        if(ex.equals(deviceO.get("exchange"))){
                            deviceService.removeCarAndDevice(obdCode,0L);
                        }else{
                            if(deviceO.get("exchange").equals(Constant.EXCHANGE_LPS)) return Result.error("该设备可能已在工程车系统绑定车辆，请尝试登录该平台解绑！");
                            if(deviceO.get("exchange").equals(Constant.EXCHANGE_XF)) return Result.error("该设备可能已在消防车系统绑定车辆，请尝试登录该平台解绑！");
                            if(deviceO.get("exchange").equals(Constant.EXCHANGE_YXCL)) return Result.error("该设备可能已在公务车系统绑定车辆，请尝试登录该平台解绑！");
                        }
                    }
                }
            }

            return Result.success();
        } catch (Exception e) {
            logger.error("app/obd/checkDeviceAndCarNo", e);
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation(value = "设备更新", httpMethod = "POST", response = Result.class, notes = "必须的参数：{vehicnum:车牌号,obdnum:终端编号,simnum:SIM号,odometer:里程表读数,connectway:接线方式,org:机构id}")
    @RequestMapping(value="app/obd/changeDevice", method = RequestMethod.POST)
    @ResponseBody
    public Result changeDevice(HttpServletRequest request) {
        try {
            String uid = request.getParameter("uid");
            String car_id = request.getParameter("car_id");
            String device_id = request.getParameter("device_id");
            String vehicnum = request.getParameter("vehicnum");
            String car_no_color = request.getParameter("car_no_color");
            String self_num = request.getParameter("selfnum");
            String car_brand = request.getParameter("car_brand");
            String type = request.getParameter("type");
            String fule_type = request.getParameter("fule_type");
            String driver_image_1 = request.getParameter("driver_image_1");
            String driver_image_2 = request.getParameter("driver_image_2");
            String tmp_tv = request.getParameter("tank_vol");
            String tmp_tl = request.getParameter("tank_length");
            String tmp_th = request.getParameter("tank_height");
            String tmp_tw = request.getParameter("tank_width");
            Double tank_vol = null,tank_length = null,tank_height = null,tank_width = null;
            if(StringUtils.isNotBlank(tmp_tv)){
                tank_vol = Double.valueOf(tmp_tv);
            }
            if(StringUtils.isNotBlank(tmp_tl)){
                tank_length = Double.valueOf(tmp_tl);
            }
            if(StringUtils.isNotBlank(tmp_th)){
                tank_height = Double.valueOf(tmp_th);
            }
            if(StringUtils.isNotBlank(tmp_tw)){
                tank_width = Double.valueOf(tmp_tw);
            }
            String obdnum = request.getParameter("obdnum");
            String simnum = request.getParameter("simnum");
            String odometer = request.getParameter("odometer");
            String connectway = request.getParameter("connectway");
            String remark = request.getParameter("remark");
            String org = request.getParameter("org");
            String vin = request.getParameter("vin");
            String device_type = request.getParameter("device_type");
            String video_device = request.getParameter("video_device");
            String video_sim = request.getParameter("video_sim");
            String extend_fuel = request.getParameter("extend_fuel");
            String extend_trun = request.getParameter("extend_trun");
            String extend_water = request.getParameter("extend_water");
            String cameraCount = request.getParameter("cameracount");//摄像头个数
            cameraCount = cameraCount == null || cameraCount.trim().equals("")? "0" : cameraCount;
            String screenCount = request.getParameter("screencount");//显示屏个数
            screenCount = screenCount == null || screenCount.trim().equals("")? "0" : screenCount;
            String interphoneCount = request.getParameter("interphonecount");//对讲机套数
            interphoneCount = interphoneCount == null || interphoneCount.trim().equals("")? "0" : interphoneCount;
            String warnerCount = request.getParameter("warnercount");//报警器套数
            warnerCount = warnerCount == null || warnerCount.trim().equals("")? "0" : warnerCount;
            String extend = extend_fuel + "," + extend_water + "," + extend_trun;

            //搜索车辆信息
			Device upDevice = null;
			Device newDevice = null;
			Car upCar = null;
			Car newCar = null;
            HashMap<String, Object> upMap = new HashMap<String, Object>();
			upMap.put("id", device_id);
			upMap.put("status", Constant.ADD_STATUS);
			upDevice = deviceService.findByMap(upMap);
			upMap.clear();
			upMap.put("id", car_id);
			upMap.put("status", Constant.ADD_STATUS);
			upCar = carService.findByMap(upMap);
            if(upCar.getCar_no().equals(vehicnum)) {
				upCar.setCar_no_color(car_no_color);
				upCar.setSelf_num(self_num);
				upCar.setBrand(car_brand);
				upCar.setType(type);
				upCar.setObd(Constant.BOUND_OBD);
				upCar.setFule_type(fule_type);
				upCar.setTank_vol(tank_vol);
				upCar.setTank_length(tank_length);
				upCar.setTank_height(tank_height);
				upCar.setTank_width(tank_width);
				driver_image_1 = uploadFile(driver_image_1);
				upCar.setDriver_image_1(driver_image_1);
				driver_image_2 = uploadFile(driver_image_2);
				upCar.setDriver_image_2(driver_image_2);
				upCar.setVin(vin);
				if (StringUtils.isNotBlank(remark)) {
					upCar.setTexts(remark);
				}
				if (StringUtils.isBlank(odometer)) {
					long k = 0;
					upCar.setMileage(k);
				} else {
					upCar.setMileage(Long.parseLong(odometer));
				}
				upCar.setOrg(Long.parseLong(org));
				upCar.setCreatedby(Long.parseLong(uid));
				upCar.setUpdatetime(DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
			}else{
				newCar = new Car();
				newCar.setCar_no(vehicnum);
				newCar.setCar_no_color(car_no_color);
				newCar.setSelf_num(self_num);
				newCar.setBrand(car_brand);
				newCar.setType(type);
				newCar.setFule_type(fule_type);
				newCar.setTank_vol(tank_vol);
				newCar.setTank_length(tank_length);
				newCar.setTank_height(tank_height);
				newCar.setTank_width(tank_width);
				driver_image_1 = uploadFile(driver_image_1);
				newCar.setDriver_image_1(driver_image_1);
				driver_image_2 = uploadFile(driver_image_2);
				newCar.setDriver_image_2(driver_image_2);
				newCar.setVin(vin);
				if(StringUtils.isNotBlank(remark)){
					newCar.setTexts(remark);
				}
				if(StringUtils.isBlank(odometer)){
					long k = 0;
					newCar.setMileage(k);
				}else{
					newCar.setMileage(Long.parseLong(odometer));
				}
				newCar.setObd(Constant.BOUND_OBD);
				newCar.setStatus(Constant.ADD_STATUS);
				newCar.setCar_status(Constant.UN_CHECK_STATUS);
				newCar.setOrg(Long.parseLong(org));
				newCar.setCreatedby(Long.parseLong(uid));
				newCar.setCreatetime(DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
			}
			if(upDevice.getDevice().equals(obdnum)) {
				upDevice.setCar_no(vehicnum);
				upDevice.setDevice(obdnum);
				upDevice.setDevice_type(device_type);
				if (Constant.DEVICE_TYPE_VIDEO.equals(device_type)) {
					upDevice.setVideo_device(simnum);
					upDevice.setVideo_sim(video_sim);
				} else {
					upDevice.setVideo_device(video_device);
					upDevice.setVideo_sim(video_sim);
				}
				upDevice.setSim(simnum);
				upDevice.setConnectway(connectway);
				upDevice.setCameraCount(Integer.parseInt(cameraCount));
				upDevice.setScreenCount(Integer.parseInt(screenCount));
				upDevice.setInterphoneCount(Integer.parseInt(interphoneCount));
				upDevice.setWarnerCount(Integer.parseInt(warnerCount));
				upDevice.setCreatedate(DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
				upDevice.setStatus(Constant.ADD_STATUS);
				upDevice.setCreatedby(Long.parseLong(uid));
				upDevice.setExtend(extend);
			}else{
            	newDevice = new Device();
				newDevice.setCar_no(vehicnum);
				newDevice.setDevice(obdnum);
				newDevice.setDevice_type(device_type);
				if(Constant.DEVICE_TYPE_VIDEO.equals(device_type)){
					newDevice.setVideo_device(simnum);
					newDevice.setVideo_sim(video_sim);
				} else {
					newDevice.setVideo_device(video_device);
					newDevice.setVideo_sim(video_sim);
				}
				newDevice.setSim(simnum);
				newDevice.setConnectway(connectway);
				newDevice.setCreatedate(DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
				newDevice.setStatus(Constant.ADD_STATUS);
				newDevice.setCreatedby(Long.parseLong(uid));
				newDevice.setExtend(extend);
				newDevice.setCameraCount(Integer.parseInt(cameraCount));
				newDevice.setScreenCount(Integer.parseInt(screenCount));
				newDevice.setInterphoneCount(Integer.parseInt(interphoneCount));
				newDevice.setWarnerCount(Integer.parseInt(warnerCount));
			}

            deviceService.updateCarAddDevice(upDevice, upCar, newDevice, newCar);

            return Result.success("车辆设备变更成功！");
        } catch (Exception e) {
            logger.error("app/obd/change", e);
            return Result.error(e.getMessage());
        }
    }

	private boolean obdDeviceCheck(String str) {
		  String regex = "^(H)[a-zA-Z0-9]{12}$";
		  return str.matches(regex);
	}
	
	private boolean a9DeviceCheck(String str) {
		  String regex = "^[0-9]{11}$";
		  return str.matches(regex);
	}
	
	private boolean km01DeviceCheck(String str) {
		  String regex = "^[0-9]{11}$";
		  return str.matches(regex);
	}
	
	private boolean videoDeviceCheck(String str) {
		  String regex = "^[0-9]+$";
		  return str.matches(regex);
	}
	
	private boolean simCodeCheck(String str) {
		  String regex = "^[a-zA-Z0-9]+$";
		  return str.matches(regex);
	}
}
