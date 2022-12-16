---@return boolean

-- print(answer);
local list = query:getList();
for index = 1, list:size() do
    local sponsor = list:get(index - 1);
    local user = sponsor:getUser();
    -- print(user);
    if user:getUserId() == answer or user:getName() == answer then
        return true;
    end
end

return false;