using Microsoft.EntityFrameworkCore.Migrations;

namespace Core.Db.Migrations
{
    public partial class Changereviewplacelink2 : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_Reviews_Places_PlaceId1",
                schema: "citylogia",
                table: "Reviews");

            migrationBuilder.DropIndex(
                name: "IX_Reviews_PlaceId1",
                schema: "citylogia",
                table: "Reviews");

            migrationBuilder.DropColumn(
                name: "PlaceId1",
                schema: "citylogia",
                table: "Reviews");
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<long>(
                name: "PlaceId1",
                schema: "citylogia",
                table: "Reviews",
                type: "bigint",
                nullable: false,
                defaultValue: 0L);

            migrationBuilder.CreateIndex(
                name: "IX_Reviews_PlaceId1",
                schema: "citylogia",
                table: "Reviews",
                column: "PlaceId1");

            migrationBuilder.AddForeignKey(
                name: "FK_Reviews_Places_PlaceId1",
                schema: "citylogia",
                table: "Reviews",
                column: "PlaceId1",
                principalSchema: "citylogia",
                principalTable: "Places",
                principalColumn: "Id",
                onDelete: ReferentialAction.Cascade);
        }
    }
}
